package cn.bugstack.infrastructure.persistent.repository.impl;

import cn.bugstack.infrastructure.event.ActivitySkuStockZeroMessageEvent;
import cn.bugstack.infrastructure.event.EventPublisher;
import cn.bugstack.infrastructure.persistent.dao.*;
import cn.bugstack.infrastructure.persistent.po.*;
import cn.bugstack.infrastructure.persistent.redis.IRedisService;
import cn.bugstack.infrastructure.persistent.repository.IActivityRepository;
import cn.bugstack.types.aggregate.CreateOrderAggregate;
import cn.bugstack.types.common.Constants;
import cn.bugstack.types.entity.ActivityCountEntity;
import cn.bugstack.types.entity.ActivityEntity;
import cn.bugstack.types.entity.ActivityOrderEntity;
import cn.bugstack.types.entity.ActivitySkuEntity;
import cn.bugstack.types.enums.ActivityStateVO;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import cn.bugstack.types.vo.ActivitySkuStockKeyVO;
import cn.bugstack.types.vo.StrategyAwardStockKeyVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
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
    private EventPublisher eventPublisher;
    @Resource
    private ActivitySkuStockZeroMessageEvent activitySkuStockZeroMessageEvent;

    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.queryActivitySku(sku);
        return ActivitySkuEntity.builder()
                .sku(raffleActivitySku.getSku())
                .activityId(raffleActivitySku.getActivityId())
                .activityCountId(raffleActivitySku.getActivityCountId())
                .stockCount(raffleActivitySku.getStockCount())
                .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
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
    public void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
        RaffleActivityOrder raffleActivityOrder = buildRaffleActivityOrder(createOrderAggregate);
        RaffleActivityAccount raffleActivityAccount = buildRaffleActivityAccount(createOrderAggregate);
        transactionTemplate.execute(status -> {
            try {
                // 1. 写入订单
                raffleActivityOrderDao.insert(raffleActivityOrder);
                // 2. 更新账户
                int count = raffleActivityAccountDao.updateAccountQuota(raffleActivityAccount);
                if (count <= 0) {
                    raffleActivityAccountDao.insert(raffleActivityAccount);
                }
            } catch (DuplicateKeyException e) {
                status.setRollbackOnly();
                log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", createOrderAggregate.getUserId()
                        , createOrderAggregate.getActivityId(), createOrderAggregate.getActivityOrderEntity().getSku(), e);
                throw new AppException(ResponseCode.CREATE_RAFFLE_ACTIVITY_ORDER_ERROR);
            } catch (Exception e) {
                log.error("写入订单记录出错 userId: {} activityId: {} sku: {}", createOrderAggregate.getUserId()
                        , createOrderAggregate.getActivityId(), createOrderAggregate.getActivityOrderEntity().getSku(), e);
                throw new AppException(ResponseCode.CREATE_RAFFLE_ACTIVITY_ORDER_ERROR);
            }
            return 1;
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
            // todo 库存消耗没了，发mq消息
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

    private RaffleActivityAccount buildRaffleActivityAccount(CreateOrderAggregate createOrderAggregate) {
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

    private RaffleActivityOrder buildRaffleActivityOrder(CreateOrderAggregate createOrderAggregate) {
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
        return raffleActivityOrder;
    }
}
