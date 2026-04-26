package cn.bugstack.infrastructure.persistent.repository.impl;

import cn.bugstack.domain.activity.event.ActivitySkuStockZeroMessageEvent;
import cn.bugstack.domain.activity.model.aggreate.CreateQuotaOrderAggregate;
import cn.bugstack.domain.activity.model.aggreate.CreatePartakeOrderAggregate;
import cn.bugstack.domain.activity.model.entity.*;
import cn.bugstack.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import cn.bugstack.domain.activity.model.valobj.ActivityStateVO;
import cn.bugstack.domain.activity.model.valobj.UserRaffleOrderStateVO;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.domain.strategy.model.entity.ActivityEntity;
import cn.bugstack.infrastructure.event.EventPublisher;
import cn.bugstack.infrastructure.persistent.dao.*;
import cn.bugstack.infrastructure.persistent.po.*;
import cn.bugstack.infrastructure.persistent.redis.IRedisService;
import cn.bugstack.infrastructure.util.ActivityConvert;
import cn.bugstack.types.common.Constants;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.types.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RLock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class ActivityRepository implements IActivityRepository {
    @Resource
    private IRedisService redisService;
    @Resource
    private IRaffleActivityDao raffleActivityDao;
    @Resource
    private IRaffleActivitySkuDao raffleActivitySkuDao;
    @Resource
    private IRaffleActivityCountDao raffleActivityCountDao;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;
    @Resource
    private IRaffleActivityAccountDao raffleActivityAccountDao;
    @Resource
    private IRaffleActivityAccountMonthDao raffleActivityAccountMonthDao;
    @Resource
    private IRaffleActivityAccountDayDao raffleActivityAccountDayDao;

    @Resource
    private IUserRaffleOrderDao userRaffleOrderDao;

    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private ActivitySkuStockZeroMessageEvent activitySkuStockZeroMessageEvent;

    @Resource
    private ActivityConvert convert;

    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.queryActivitySku(sku);
        return ActivitySkuEntity.builder()
                .sku(raffleActivitySku.getSku())
                .activityId(raffleActivitySku.getActivityId())
                .activityCountId(raffleActivitySku.getActivityCountId())
                .stockCount(raffleActivitySku.getStockCount())
                .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
                .productAmount(raffleActivitySku.getProductAmount())
                .build();
    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        // 优先从缓存获取
        String cacheKey = Constants.ACTIVITY_KEY(activityId);
        ActivityEntity activityEntity = redisService.getValue(cacheKey);
        if (null != activityEntity) return activityEntity;
        // 从库中获取数据
        RaffleActivity raffleActivity = raffleActivityDao.queryRaffleActivityByActivityId(activityId);
        activityEntity = ActivityEntity.builder()
                .activityId(raffleActivity.getActivityId())
                .activityName(raffleActivity.getActivityName())
                .activityDesc(raffleActivity.getActivityDesc())
                .beginDateTime(raffleActivity.getBeginDateTime())
                .endDateTime(raffleActivity.getEndDateTime())
                .strategyId(raffleActivity.getStrategyId())
                .state(ActivityStateVO.valueOf(raffleActivity.getState()))
                .build();
        redisService.setValue(cacheKey, activityEntity);
        return activityEntity;
    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        // 优先从缓存获取
        String cacheKey = Constants.ACTIVITY_COUNT_KEY(activityCountId);
        ActivityCountEntity activityCountEntity = redisService.getValue(cacheKey);
        if (null != activityCountEntity) return activityCountEntity;
        // 从库中获取数据
        RaffleActivityCount raffleActivityCount = raffleActivityCountDao.queryRaffleActivityCountByActivityCountId(activityCountId);
        activityCountEntity = ActivityCountEntity.builder()
                .activityCountId(raffleActivityCount.getActivityCountId())
                .totalCount(raffleActivityCount.getTotalCount())
                .dayCount(raffleActivityCount.getDayCount())
                .monthCount(raffleActivityCount.getMonthCount())
                .build();
        redisService.setValue(cacheKey, activityCountEntity);
        return activityCountEntity;
    }

    @Override
    public void doSaveCreditNoPayOrder(CreateQuotaOrderAggregate createOrderAggregate) {
        RaffleActivityOrder raffleActivityOrder = this.buildRaffleActivityOrder(createOrderAggregate);
        RaffleActivityAccount raffleActivityAccount = this.buildRaffleActivityAccount(createOrderAggregate);
        RaffleActivityAccountMonth raffleActivityAccountMonth = this.buildRaffleActivityAccountMonth(createOrderAggregate);
        RaffleActivityAccountDay raffleActivityAccountDay = this.buildRaffleActivityAccountDay(createOrderAggregate);
        transactionTemplate.execute(status -> {
            try {
                // 1. 写入订单
                raffleActivityOrderDao.insert(raffleActivityOrder);
                // 2. 更新账户-总
                int count = raffleActivityAccountDao.addAccountQuota(raffleActivityAccount);
                if (count <= 0) {
                    raffleActivityAccountDao.insert(raffleActivityAccount);
                }

                // 更新账户-月
                int countMonth = raffleActivityAccountMonthDao.addAccountQuota(raffleActivityAccountMonth);
                if (countMonth <= 0) {
                    raffleActivityAccountMonthDao.insert(raffleActivityAccountMonth);
                }

                //更新账户-日
                int countDay = raffleActivityAccountDayDao.addAccountQuota(raffleActivityAccountDay);
                if (countDay <= 0) {
                    raffleActivityAccountDayDao.insert(raffleActivityAccountDay);
                }

            } catch (DuplicateKeyException e) {
                status.setRollbackOnly();
                log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", createOrderAggregate.getUserId()
                        , createOrderAggregate.getActivityId(), createOrderAggregate.getActivityOrderEntity().getSku(), e);
                throw new AppException(ResponseCode.CREATE_RAFFLE_ACTIVITY_ORDER_ERROR);
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("写入订单记录出错 userId: {} activityId: {} sku: {}", createOrderAggregate.getUserId()
                        , createOrderAggregate.getActivityId(), createOrderAggregate.getActivityOrderEntity().getSku(), e);
                throw new AppException(ResponseCode.CREATE_RAFFLE_ACTIVITY_ORDER_ERROR);
            }
            return 1;
        });
    }

    @Override
    public void doSaveCreditPayOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        RaffleActivityOrder raffleActivityOrder = this.buildRaffleActivityOrder(createQuotaOrderAggregate);

        transactionTemplate.execute(status -> {
            try {
                raffleActivityOrderDao.insert(raffleActivityOrder);
                return 1;
            } catch (DuplicateKeyException e) {
                log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", createQuotaOrderAggregate.getUserId()
                        , createQuotaOrderAggregate.getActivityId(), createQuotaOrderAggregate.getActivityOrderEntity().getSku(), e);
                status.setRollbackOnly();
                throw new AppException(ResponseCode.INDEX_DUP);
            }
        });
    }


    @Override
    public void cacheActivitySkuStockCount(String cacheKey, Integer stockCount) {
        if (!redisService.isExists(cacheKey)) {
            redisService.setAtomicLong(cacheKey, stockCount);
        }

    }

    @Override
    public boolean subtractionActivitySkuStock(Long sku, String cacheKey, Date endDateTime) {
        long decr = redisService.decr(cacheKey);
        if (decr == 0) {
            eventPublisher.publish(activitySkuStockZeroMessageEvent.topic(),
                    activitySkuStockZeroMessageEvent.buildEventMessage(sku));
            return true;
        } else if (decr < 0) {
            redisService.setAtomicLong(cacheKey, 0);
            return false;
        }
        // 1 按照cachekey decr后的值如 99 98 97 和key组成为库存锁的key进行使用
        // 2 加锁兜底
        // 3 设置加锁时间 活动到期加一天
        String lockKey = cacheKey + Constants.UNDERLINE + decr;
        long lockTime = endDateTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        Boolean lock = redisService.setNx(lockKey, lockTime, TimeUnit.MILLISECONDS);
        if (!lock) {
            log.info("活动sku库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    @Override
    public void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO activitySkuStockKeyVO) {
        String key = Constants.STRATEGY_SKU_COUNT_QUEUE_KEY();
        RBlockingQueue<ActivitySkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(key);
        RDelayedQueue<ActivitySkuStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.offer(activitySkuStockKeyVO, 3, TimeUnit.SECONDS);
    }

    @Override
    public ActivitySkuStockKeyVO takeQueueValue() throws InterruptedException {
        String key = Constants.STRATEGY_SKU_COUNT_QUEUE_KEY();
        RBlockingQueue<ActivitySkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(key);
        return blockingQueue.poll();
    }

    @Override
    public void clearQueueValue() {
        String key = Constants.STRATEGY_SKU_COUNT_QUEUE_KEY();
        RBlockingQueue<ActivitySkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(key);
        blockingQueue.clear();
    }

    @Override
    public void updateActivitySkuStock(Long sku) {
        raffleActivitySkuDao.updateActivitySkuStock(sku);
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        raffleActivitySkuDao.clearActivitySkuStock(sku);

    }

    @Override
    public ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId) {
        // 1. 查询账户
        RaffleActivityAccount raffleActivityAccountReq = new RaffleActivityAccount();
        raffleActivityAccountReq.setUserId(userId);
        raffleActivityAccountReq.setActivityId(activityId);
        RaffleActivityAccount raffleActivityAccountRes = raffleActivityAccountDao.queryActivityAccountByUserId(raffleActivityAccountReq);
        if (null == raffleActivityAccountRes) return null;
        // 2. 转换对象
        return ActivityAccountEntity.builder()
                .userId(raffleActivityAccountRes.getUserId())
                .activityId(raffleActivityAccountRes.getActivityId())
                .totalCount(raffleActivityAccountRes.getTotalCount())
                .totalCountSurplus(raffleActivityAccountRes.getTotalCountSurplus())
                .dayCount(raffleActivityAccountRes.getDayCount())
                .dayCountSurplus(raffleActivityAccountRes.getDayCountSurplus())
                .monthCount(raffleActivityAccountRes.getMonthCount())
                .monthCountSurplus(raffleActivityAccountRes.getMonthCountSurplus())
                .build();
    }

    @Override
    public ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Long activityId, String month) {
        // 1. 查询账户
        RaffleActivityAccountMonth raffleActivityAccountMonthReq = new RaffleActivityAccountMonth();
        raffleActivityAccountMonthReq.setUserId(userId);
        raffleActivityAccountMonthReq.setActivityId(activityId);
        raffleActivityAccountMonthReq.setMonth(month);
        RaffleActivityAccountMonth raffleActivityAccountMonthRes = raffleActivityAccountMonthDao.queryActivityAccountMonthByUserId(raffleActivityAccountMonthReq);
        if (null == raffleActivityAccountMonthRes) return null;
        // 2. 转换对象
        return ActivityAccountMonthEntity.builder()
                .userId(raffleActivityAccountMonthRes.getUserId())
                .activityId(raffleActivityAccountMonthRes.getActivityId())
                .month(raffleActivityAccountMonthRes.getMonth())
                .monthCount(raffleActivityAccountMonthRes.getMonthCount())
                .monthCountSurplus(raffleActivityAccountMonthRes.getMonthCountSurplus())
                .build();
    }

    @Override
    public ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day) {
        // 1. 查询账户
        RaffleActivityAccountDay raffleActivityAccountDayReq = new RaffleActivityAccountDay();
        raffleActivityAccountDayReq.setUserId(userId);
        raffleActivityAccountDayReq.setActivityId(activityId);
        raffleActivityAccountDayReq.setDay(day);
        RaffleActivityAccountDay raffleActivityAccountDayRes = raffleActivityAccountDayDao.queryActivityAccountDayByUserId(raffleActivityAccountDayReq);
        if (null == raffleActivityAccountDayRes) return null;
        // 2. 转换对象
        return ActivityAccountDayEntity.builder()
                .userId(raffleActivityAccountDayRes.getUserId())
                .activityId(raffleActivityAccountDayRes.getActivityId())
                .day(raffleActivityAccountDayRes.getDay())
                .dayCount(raffleActivityAccountDayRes.getDayCount())
                .dayCountSurplus(raffleActivityAccountDayRes.getDayCountSurplus())
                .build();
    }

    @Override
    public UserRaffleOrderEntity queryNoUsedRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {
        // 查询数据
        UserRaffleOrder userRaffleOrderReq = new UserRaffleOrder();
        userRaffleOrderReq.setUserId(partakeRaffleActivityEntity.getUserId());
        userRaffleOrderReq.setActivityId(partakeRaffleActivityEntity.getActivityId());
        UserRaffleOrder userRaffleOrderRes = userRaffleOrderDao.queryNoUsedRaffleOrder(userRaffleOrderReq);

        if (null == userRaffleOrderRes) return null;
        // 封装结果
        UserRaffleOrderEntity userRaffleOrderEntity = new UserRaffleOrderEntity();
        userRaffleOrderEntity.setUserId(userRaffleOrderRes.getUserId());
        userRaffleOrderEntity.setActivityId(userRaffleOrderRes.getActivityId());
        userRaffleOrderEntity.setActivityName(userRaffleOrderRes.getActivityName());
        userRaffleOrderEntity.setStrategyId(userRaffleOrderRes.getStrategyId());
        userRaffleOrderEntity.setOrderId(userRaffleOrderRes.getOrderId());
        userRaffleOrderEntity.setOrderTime(userRaffleOrderRes.getOrderTime());
        userRaffleOrderEntity.setOrderState(UserRaffleOrderStateVO.valueOf(userRaffleOrderRes.getOrderState()));
        return userRaffleOrderEntity;
    }

    @Override
    public void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate) {
        String userId = createPartakeOrderAggregate.getUserId();
        Long activityId = createPartakeOrderAggregate.getActivityId();
        ActivityAccountEntity activityAccountEntity = createPartakeOrderAggregate.getActivityAccountEntity();
        ActivityAccountMonthEntity activityAccountMonthEntity = createPartakeOrderAggregate.getActivityAccountMonthEntity();
        ActivityAccountDayEntity activityAccountDayEntity = createPartakeOrderAggregate.getActivityAccountDayEntity();
        UserRaffleOrderEntity userRaffleOrderEntity = createPartakeOrderAggregate.getUserRaffleOrderEntity();

        transactionTemplate.execute(status -> {
            try {
                // 更新总账户
                int totalCount = raffleActivityAccountDao.updateActivityAccountSubtractQuota(RaffleActivityAccount
                        .builder()
                        .userId(userId)
                        .activityId(activityId)
                        .build()
                );
                if (totalCount != 1) {
                    status.setRollbackOnly();
                    log.error("写入创建参与活动 总账户额度不足，异常 userId{} activityId{}", userId, activityId);
                    throw new AppException(ResponseCode.CREATE_RAFFLE_ACTIVITY_ORDER_ERROR);
                }
                // 更新月账户
                if (createPartakeOrderAggregate.isExistAccountMonth()) {
                    int totalCountMonth = raffleActivityAccountMonthDao.updateActivityAccountSubtractQuota(RaffleActivityAccountMonth
                            .builder()
                            .userId(userId)
                            .activityId(activityId)
                            .month(activityAccountMonthEntity.getMonth())
                            .build()
                    );

                    if (totalCountMonth != 1) {
                        status.setRollbackOnly();
                        log.error("写入创建参与活动 月度账户额度不足，异常 userId{} activityId{}", userId, activityId);
                        throw new AppException(ResponseCode.CREATE_RAFFLE_ACTIVITY_ORDER_ERROR);
                    }
                } else {
                    raffleActivityAccountMonthDao.insert(RaffleActivityAccountMonth
                            .builder()
                            .userId(userId)
                            .activityId(activityId)
                            .month(activityAccountMonthEntity.getMonth())
                            .monthCount(activityAccountMonthEntity.getMonthCount())
                            .monthCountSurplus(activityAccountMonthEntity.getMonthCountSurplus() - 1)
                            .build()
                    );

                    // 新创建用户，更新总账户表中的月镜像额度
                    raffleActivityAccountDao.updateActivityAccountMonthSurplusSubtractQuota(RaffleActivityAccount
                            .builder()
                            .userId(userId)
                            .activityId(activityId)
                            .monthCountSurplus(activityAccountEntity.getMonthCountSurplus())
                            .build()
                    );
                }

                // 更新日账户
                if (createPartakeOrderAggregate.isExistAccountDay()) {
                    int totalCountDay = raffleActivityAccountDayDao.updateActivityAccountSubtractQuota(RaffleActivityAccountDay
                            .builder()
                            .userId(userId)
                            .activityId(activityId)
                            .day(activityAccountDayEntity.getDay())
                            .build()
                    );

                    if (totalCountDay != 1) {
                        status.setRollbackOnly();
                        log.error("写入创建参与活动 日度账户额度不足，异常 userId{} activityId{}", userId, activityId);
                        throw new AppException(ResponseCode.CREATE_RAFFLE_ACTIVITY_ORDER_ERROR);
                    }
                } else {
                    raffleActivityAccountDayDao.insert(RaffleActivityAccountDay
                            .builder()
                            .userId(userId)
                            .activityId(activityId)
                            .day(activityAccountDayEntity.getDay())
                            .dayCount(activityAccountDayEntity.getDayCount())
                            .dayCountSurplus(activityAccountDayEntity.getDayCountSurplus() - 1)
                            .build()
                    );

                    // 新创建用户，更新总账户表中的月镜像额度
                    raffleActivityAccountDao.updateActivityAccountDaySurplusImageQuota(RaffleActivityAccount
                            .builder()
                            .userId(userId)
                            .activityId(activityId)
                            .dayCountSurplus(activityAccountEntity.getDayCountSurplus())
                            .build()
                    );
                }
                // 4. 写入参与活动订单
                userRaffleOrderDao.insert(UserRaffleOrder.builder()
                        .userId(userRaffleOrderEntity.getUserId())
                        .activityId(userRaffleOrderEntity.getActivityId())
                        .activityName(userRaffleOrderEntity.getActivityName())
                        .strategyId(userRaffleOrderEntity.getStrategyId())
                        .orderId(userRaffleOrderEntity.getOrderId())
                        .orderTime(userRaffleOrderEntity.getOrderTime())
                        .orderState(userRaffleOrderEntity.getOrderState().getCode())
                        .build());
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("写入创建参与活动 记录失败 userId{} activityId{}", userId, activityId, e);
                throw new AppException(ResponseCode.CREATE_RAFFLE_ACTIVITY_ORDER_ERROR);
            }
            return 1;
        });

    }

    @Override
    public List<ActivitySkuEntity> queryRaffleActivitySkuListByActivityId(Long activityId) {
        List<RaffleActivitySku> activitySkuEntityList = raffleActivitySkuDao.queryActivitySkuListByActivityId(activityId);
        List<ActivitySkuEntity> result = new ArrayList<ActivitySkuEntity>();
        for (RaffleActivitySku raffleActivitySku : activitySkuEntityList) {
            ActivitySkuEntity activitySkuEntity = new ActivitySkuEntity();
            activitySkuEntity.setActivityId(raffleActivitySku.getActivityId());
            activitySkuEntity.setActivityCountId(raffleActivitySku.getActivityCountId());
            activitySkuEntity.setStockCount(raffleActivitySku.getStockCount());
            activitySkuEntity.setStockCountSurplus(activitySkuEntity.getStockCountSurplus());
            activitySkuEntity.setSku(raffleActivitySku.getSku());
            result.add(activitySkuEntity);
        }
        return result;
    }

    @Override
    public Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId) {
        RaffleActivityAccountDay raffleActivityAccountDayReq = new RaffleActivityAccountDay();
        raffleActivityAccountDayReq.setUserId(userId);
        raffleActivityAccountDayReq.setActivityId(activityId);
        raffleActivityAccountDayReq.setDay(raffleActivityAccountDayReq.currentDay());
        Integer dayPartakeCount = raffleActivityAccountDayDao.queryRaffleActivityAccountDayPartakeCount(raffleActivityAccountDayReq);
        return null == dayPartakeCount ? 0 : dayPartakeCount;
    }

    @Override
    public ActivityAccountEntity queryActivityAccount(String userId, Long activityId) {
        RaffleActivityAccount account = new RaffleActivityAccount();
        account.setUserId(userId);
        account.setActivityId(activityId);
        RaffleActivityAccount raffleActivityAccount = raffleActivityAccountDao.queryActivityAccountByUserId(account);
        if (null == raffleActivityAccount) {
            return ActivityAccountEntity.builder()
                    .activityId(activityId)
                    .userId(userId)
                    .totalCount(0)
                    .totalCountSurplus(0)
                    .monthCount(0)
                    .monthCountSurplus(0)
                    .dayCount(0)
                    .dayCountSurplus(0)
                    .build();
        }
        // 2. 查询月账户额度
        RaffleActivityAccountMonth raffleActivityAccountMonth = raffleActivityAccountMonthDao.queryActivityAccountMonthByUserId(RaffleActivityAccountMonth.builder()
                .activityId(activityId)
                .userId(userId)
                .build());

        // 3. 查询日账户额度
        RaffleActivityAccountDay raffleActivityAccountDay = raffleActivityAccountDayDao.queryActivityAccountDayByUserId(RaffleActivityAccountDay.builder()
                .activityId(activityId)
                .userId(userId)
                .build());

        // 组装对象
        ActivityAccountEntity activityAccountEntity = new ActivityAccountEntity();
        activityAccountEntity.setUserId(userId);
        activityAccountEntity.setActivityId(activityId);
        activityAccountEntity.setTotalCount(raffleActivityAccount.getTotalCount());
        activityAccountEntity.setTotalCountSurplus(raffleActivityAccount.getTotalCountSurplus());

        // 如果没有创建日账户，则从总账户中获取日总额度填充。「当新创建日账户时，会获得总账户额度」
        if (null == raffleActivityAccountDay) {
            activityAccountEntity.setDayCount(raffleActivityAccount.getDayCount());
            activityAccountEntity.setDayCountSurplus(raffleActivityAccount.getDayCount());
        } else {
            activityAccountEntity.setDayCount(raffleActivityAccountDay.getDayCount());
            activityAccountEntity.setDayCountSurplus(raffleActivityAccountDay.getDayCountSurplus());
        }

        // 如果没有创建月账户，则从总账户中获取月总额度填充。「当新创建日账户时，会获得总账户额度」
        if (null == raffleActivityAccountMonth) {
            activityAccountEntity.setMonthCount(raffleActivityAccount.getMonthCount());
            activityAccountEntity.setMonthCountSurplus(raffleActivityAccount.getMonthCount());
        } else {
            activityAccountEntity.setMonthCount(raffleActivityAccountMonth.getMonthCount());
            activityAccountEntity.setMonthCountSurplus(raffleActivityAccountMonth.getMonthCountSurplus());
        }

        return activityAccountEntity;

    }

    @Override
    public Integer queryRaffleActivityAccountPartakeCount(Long activityId, String userId) {
        RaffleActivityAccount account = new RaffleActivityAccount();
        account.setUserId(userId);
        account.setActivityId(activityId);
        RaffleActivityAccount raffleActivityAccount = raffleActivityAccountDao.queryActivityAccountByUserId(account);
        return null == raffleActivityAccount.getTotalCount() ? 0 : raffleActivityAccount.getTotalCountSurplus();
    }

    @Override
    public void updateSkuRechargeOrder(DeliveryOrderEntity deliveryOrderEntity) {
        RLock lock = redisService.getLock(Constants.UPDATE_USER_CREDIT_ACCOUNT_LOCK(deliveryOrderEntity.getUserId(), deliveryOrderEntity.getOutBusinessNo()));
        try {
            lock.lock(3, TimeUnit.SECONDS);
            RaffleActivityOrder req = new RaffleActivityOrder();
            req.setUserId(deliveryOrderEntity.getUserId());
            req.setOutBusinessNo(deliveryOrderEntity.getOutBusinessNo());
            RaffleActivityOrder raffleActivityOrderRes = raffleActivityOrderDao.queryRaffleActivityOrder(req);
            if (raffleActivityOrderRes == null) {
                return;
            }
            // 账户对象 - 总
            RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
            raffleActivityAccount.setUserId(raffleActivityOrderRes.getUserId());
            raffleActivityAccount.setActivityId(raffleActivityOrderRes.getActivityId());
            raffleActivityAccount.setTotalCount(raffleActivityOrderRes.getTotalCount());
            raffleActivityAccount.setTotalCountSurplus(raffleActivityOrderRes.getTotalCount());
            raffleActivityAccount.setDayCount(raffleActivityOrderRes.getDayCount());
            raffleActivityAccount.setDayCountSurplus(raffleActivityOrderRes.getDayCount());
            raffleActivityAccount.setMonthCount(raffleActivityOrderRes.getMonthCount());
            raffleActivityAccount.setMonthCountSurplus(raffleActivityOrderRes.getMonthCount());

            // 账户对象 - 月
            RaffleActivityAccountMonth raffleActivityAccountMonth = new RaffleActivityAccountMonth();
            raffleActivityAccountMonth.setUserId(raffleActivityOrderRes.getUserId());
            raffleActivityAccountMonth.setActivityId(raffleActivityOrderRes.getActivityId());
            raffleActivityAccountMonth.setMonth(RaffleActivityAccountMonth.currDay());
            raffleActivityAccountMonth.setMonthCount(raffleActivityOrderRes.getMonthCount());
            raffleActivityAccountMonth.setMonthCountSurplus(raffleActivityOrderRes.getMonthCount());

            // 账户对象 - 日
            RaffleActivityAccountDay raffleActivityAccountDay = new RaffleActivityAccountDay();
            raffleActivityAccountDay.setUserId(raffleActivityOrderRes.getUserId());
            raffleActivityAccountDay.setActivityId(raffleActivityOrderRes.getActivityId());
            raffleActivityAccountDay.setDay(RaffleActivityAccountDay.currentDay());
            raffleActivityAccountDay.setDayCount(raffleActivityOrderRes.getDayCount());
            raffleActivityAccountDay.setDayCountSurplus(raffleActivityOrderRes.getDayCount());
            // 编程式事务
            transactionTemplate.execute(status -> {
                try {
                    // 1. 更新订单
                    int updateCount = raffleActivityOrderDao.updateOrderCompleted(req);
                    if (1 != updateCount) {
                        status.setRollbackOnly();
                        return 1;
                    }
                    // 2. 更新账户 - 总
                    RaffleActivityAccount raffleActivityAccountRes = raffleActivityAccountDao.queryActivityAccountByUserId(raffleActivityAccount);
                    if (null == raffleActivityAccountRes) {
                        raffleActivityAccountDao.insert(raffleActivityAccount);
                    } else {
                        raffleActivityAccountDao.addAccountQuota(raffleActivityAccount);
                    }
                    // 4. 更新账户 - 月
                    raffleActivityAccountMonthDao.addAccountQuota(raffleActivityAccountMonth);
                    // 5. 更新账户 - 日
                    raffleActivityAccountDayDao.addAccountQuota(raffleActivityAccountDay);
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("更新订单记录，完成态，唯一索引冲突 userId: {} outBusinessNo: {}", deliveryOrderEntity.getUserId(), deliveryOrderEntity.getOutBusinessNo(), e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            lock.unlock();
        }

    }

    @Override
    public ActivityOrderEntity  queryUnpayActivityOrder(SkuRechargeEntity skuRechargeEntity) {
        RaffleActivityOrder req =  new RaffleActivityOrder();
        req.setUserId(skuRechargeEntity.getUserId());
        req.setSku(skuRechargeEntity.getSku());
        RaffleActivityOrder raffleActivityOrder = raffleActivityOrderDao.queryUnpayActivityOrder(req);
        if (null == raffleActivityOrder) {
            return null;
        }
        return convert.RaffleActivityOrderEntityConvert(raffleActivityOrder);
    }

    @Override
    public List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId) {
        List<RaffleActivitySku> raffleActivitySkus = raffleActivitySkuDao.queryActivitySkuListByActivityId(activityId);
        List<SkuProductEntity> skuProductEntities = new ArrayList<>(raffleActivitySkus.size());
        for (RaffleActivitySku raffleActivitySku : raffleActivitySkus) {
            RaffleActivityCount raffleActivityCount = raffleActivityCountDao.queryRaffleActivityCountByActivityCountId(raffleActivitySku.getActivityCountId());

            SkuProductEntity.ActivityCount activityCount = new SkuProductEntity.ActivityCount();
            activityCount.setTotalCount(raffleActivityCount.getTotalCount());
            activityCount.setMonthCount(raffleActivityCount.getMonthCount());
            activityCount.setDayCount(raffleActivityCount.getDayCount());

            skuProductEntities.add(SkuProductEntity.builder()
                    .sku(raffleActivitySku.getSku())
                    .activityId(raffleActivitySku.getActivityId())
                    .activityCountId(raffleActivitySku.getActivityCountId())
                    .stockCount(raffleActivitySku.getStockCount())
                    .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
                    .productAmount(raffleActivitySku.getProductAmount())
                    .activityCount(activityCount)
                    .build());

        }
        return skuProductEntities;
    }


    private RaffleActivityAccount buildRaffleActivityAccount(CreateQuotaOrderAggregate createOrderAggregate) {
        // 账户对象
        RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
        raffleActivityAccount.setUserId(createOrderAggregate.getUserId());
        raffleActivityAccount.setActivityId(createOrderAggregate.getActivityId());
        raffleActivityAccount.setTotalCount(createOrderAggregate.getTotalCount());
        raffleActivityAccount.setTotalCountSurplus(createOrderAggregate.getTotalCount());
        raffleActivityAccount.setDayCount(createOrderAggregate.getDayCount());
        raffleActivityAccount.setDayCountSurplus(createOrderAggregate.getDayCount());
        raffleActivityAccount.setMonthCount(createOrderAggregate.getMonthCount());
        raffleActivityAccount.setMonthCountSurplus(createOrderAggregate.getMonthCount());
        return raffleActivityAccount;
    }

    private RaffleActivityAccountDay buildRaffleActivityAccountDay(CreateQuotaOrderAggregate aggregate) {
        RaffleActivityAccountDay raffleActivityAccountDay = new RaffleActivityAccountDay();
        raffleActivityAccountDay.setUserId(aggregate.getUserId());
        raffleActivityAccountDay.setActivityId(aggregate.getActivityId());
        raffleActivityAccountDay.setDay(raffleActivityAccountDay.currentDay());
        raffleActivityAccountDay.setDayCount(aggregate.getDayCount());
        raffleActivityAccountDay.setDayCountSurplus(aggregate.getDayCount());

        return raffleActivityAccountDay;
    }

    private RaffleActivityAccountMonth buildRaffleActivityAccountMonth(CreateQuotaOrderAggregate aggregate) {
        RaffleActivityAccountMonth raffleActivityAccountMonth = new RaffleActivityAccountMonth();
        raffleActivityAccountMonth.setUserId(aggregate.getUserId());
        raffleActivityAccountMonth.setActivityId(aggregate.getActivityId());
        raffleActivityAccountMonth.setMonth(raffleActivityAccountMonth.currDay());
        raffleActivityAccountMonth.setMonthCount(aggregate.getMonthCount());
        raffleActivityAccountMonth.setMonthCountSurplus(aggregate.getMonthCount());

        return raffleActivityAccountMonth;
    }


    private RaffleActivityOrder buildRaffleActivityOrder(CreateQuotaOrderAggregate createOrderAggregate) {
        // 订单对象
        ActivityOrderEntity activityOrderEntity = createOrderAggregate.getActivityOrderEntity();
        RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
        raffleActivityOrder.setUserId(activityOrderEntity.getUserId());
        raffleActivityOrder.setSku(activityOrderEntity.getSku());
        raffleActivityOrder.setActivityId(activityOrderEntity.getActivityId());
        raffleActivityOrder.setActivityName(activityOrderEntity.getActivityName());
        raffleActivityOrder.setStrategyId(activityOrderEntity.getStrategyId());
        raffleActivityOrder.setOrderId(activityOrderEntity.getOrderId());
        raffleActivityOrder.setOrderTime(activityOrderEntity.getOrderTime());
        raffleActivityOrder.setTotalCount(activityOrderEntity.getTotalCount());
        raffleActivityOrder.setDayCount(activityOrderEntity.getDayCount());
        raffleActivityOrder.setMonthCount(activityOrderEntity.getMonthCount());
        raffleActivityOrder.setTotalCount(createOrderAggregate.getTotalCount());
        raffleActivityOrder.setDayCount(createOrderAggregate.getDayCount());
        raffleActivityOrder.setMonthCount(createOrderAggregate.getMonthCount());
        raffleActivityOrder.setState(activityOrderEntity.getState().getCode());
        raffleActivityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());
        raffleActivityOrder.setPayAmount(activityOrderEntity.getPayAmount());
        return raffleActivityOrder;
    }
}
