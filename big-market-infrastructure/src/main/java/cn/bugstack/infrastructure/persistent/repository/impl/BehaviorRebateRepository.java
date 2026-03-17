package cn.bugstack.infrastructure.persistent.repository.impl;

import cn.bugstack.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import cn.bugstack.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.bugstack.domain.rebate.model.valobj.BehaviorTypeVO;
import cn.bugstack.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import cn.bugstack.domain.rebate.repository.IBehaviorRebateRepository;
import cn.bugstack.infrastructure.event.EventPublisher;
import cn.bugstack.infrastructure.persistent.dao.IDailyBehaviorRebateDao;
import cn.bugstack.infrastructure.persistent.dao.ITaskDao;
import cn.bugstack.infrastructure.persistent.dao.IUserBehaviorRebateOrderDao;
import cn.bugstack.infrastructure.persistent.po.DailyBehaviorRebate;
import cn.bugstack.infrastructure.persistent.po.Task;
import cn.bugstack.infrastructure.persistent.po.UserBehaviorRebateOrder;
import cn.bugstack.infrastructure.util.BehaviorRebateConvert;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.types.common.TaskEntity;
import cn.bugstack.types.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
@Slf4j
public class BehaviorRebateRepository implements IBehaviorRebateRepository {
    @Resource
    private IDailyBehaviorRebateDao dailyBehaviorRebateDao;
    @Resource
    private IUserBehaviorRebateOrderDao userBehaviorRebateOrderDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;

    @Resource
    private BehaviorRebateConvert behaviorRebateConvert;

    @Override
    public List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO) {
        List<DailyBehaviorRebate> dailyBehaviorRebateList = dailyBehaviorRebateDao.queryDailyBehaviorRebateByBehaviorType(behaviorTypeVO.getCode());

        return behaviorRebateConvert.convertdailyBehaviorRebateList(dailyBehaviorRebateList);
    }

    @Override
    public void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates) {
        transactionTemplate.execute(status -> {
            try {
                for (BehaviorRebateAggregate aggregate : behaviorRebateAggregates) {
                    BehaviorRebateOrderEntity orderEntity = aggregate.getBehaviorRebateOrderEntity();
                    UserBehaviorRebateOrder userBehaviorRebateOrder = behaviorRebateConvert.convertBehaviorRebateOrder(orderEntity);
                    userBehaviorRebateOrderDao.insert(userBehaviorRebateOrder);

                    // 任务对象
                    TaskEntity taskEntity = aggregate.getTaskEntity();
                    Task task = behaviorRebateConvert.convertTask(taskEntity);
                    taskDao.insert(task);
                }
                return 1;
            } catch (DuplicateKeyException e) {
                status.setRollbackOnly();
                log.error("写入返利记录，唯一索引冲突 userId:{}", userId, e);
                throw new AppException(ResponseCode.INDEX_DUP);
            }
        });
    }

    @Override
    public List<BehaviorRebateOrderEntity> getOrderByOutBusinessNo(String userId, String outBusinessNo) {
        UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
        userBehaviorRebateOrder.setUserId(userId);
        userBehaviorRebateOrder.setOutBusinessNo(outBusinessNo);
        List<UserBehaviorRebateOrder> behaviorRebateOrderList = userBehaviorRebateOrderDao.queryOrderByOutBusinessNo(userBehaviorRebateOrder);
        return behaviorRebateConvert.convertUserBehaviorRebateOrderList(behaviorRebateOrderList);

    }
}
