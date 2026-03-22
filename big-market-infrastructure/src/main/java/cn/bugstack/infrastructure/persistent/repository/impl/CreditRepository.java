package cn.bugstack.infrastructure.persistent.repository.impl;

import cn.bugstack.domain.credit.model.aggregate.TradeAggregate;
import cn.bugstack.domain.credit.model.entity.CreditAccountEntity;
import cn.bugstack.domain.credit.model.entity.CreditOrderEntity;
import cn.bugstack.domain.credit.repository.ICreditRepository;
import cn.bugstack.infrastructure.event.EventPublisher;
import cn.bugstack.infrastructure.persistent.dao.ITaskDao;
import cn.bugstack.infrastructure.persistent.dao.IUserCreditAccountDao;
import cn.bugstack.infrastructure.persistent.dao.IUserCreditOrderDao;
import cn.bugstack.infrastructure.persistent.po.Task;
import cn.bugstack.infrastructure.persistent.po.UserCreditAccount;
import cn.bugstack.infrastructure.persistent.po.UserCreditOrder;
import cn.bugstack.infrastructure.persistent.redis.IRedisService;
import cn.bugstack.infrastructure.util.CreditConvert;
import cn.bugstack.types.common.Constants;
import cn.bugstack.types.common.TaskEntity;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.TimeUnit;

import static cn.bugstack.types.common.Constants.USER_CREDIT_ACCOUNT_LOCK;

@Component
@Slf4j
public class CreditRepository implements ICreditRepository {

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private IRedisService redisService;

    @Resource
    private IUserCreditAccountDao userCreditAccountDao;

    @Resource
    private IUserCreditOrderDao userCreditOrderDao;

    @Resource
    private CreditConvert convert;

    @Resource
    private ITaskDao taskDao;

    @Resource
    private EventPublisher eventPublisher;

    @Override
    public void saveUserCreditTradeOrder(TradeAggregate tradeAggregate) {
        String userId = tradeAggregate.getUserId();
        CreditAccountEntity creditAccountEntity = tradeAggregate.getCreditAccountEntity();
        CreditOrderEntity creditOrderEntity = tradeAggregate.getCreditOrderEntity();
        TaskEntity taskEntity = tradeAggregate.getTaskEntity();
        UserCreditAccount userCreditAccount = convert.creditAccountConvert(creditAccountEntity);

        UserCreditOrder userCreditOrder = convert.userCreditOrderConvert(creditOrderEntity);
        Task task = convert.taskConvert(taskEntity);
        RLock lock = redisService.getLock(USER_CREDIT_ACCOUNT_LOCK(userId, creditOrderEntity.getOutBusinessNo()));

        try {
            lock.lock(3, TimeUnit.SECONDS);
            transactionTemplate.execute(status -> {
                try {
                    // 1. 保存账户积分
                    UserCreditAccount creditAccount = userCreditAccountDao.queryUserCreditAccount(userCreditAccount);
                    if (null == creditAccount) {
                        userCreditAccountDao.insert(userCreditAccount);
                    } else {
                        userCreditAccountDao.updateUserCreditAccount(userCreditAccount);
                    }
                    // 2. 保存账户订单
                    userCreditOrderDao.insert(userCreditOrder);
                    taskDao.insert(task);

                } catch (DuplicateKeyException e) {
                    log.error("调整账户积分额度异常，唯一索引冲突 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                    status.setRollbackOnly();
                } catch (Exception e) {
                    log.error("调整账户积分额度失败 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                    status.setRollbackOnly();
                }
                return 1;
            });
        } catch (Exception e) {
            log.error("调整账户积分额度失败 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
        } finally {
            lock.unlock();
        }

        try {
            // 发送消息【在事务外执行，如果失败还有任务补偿】
            eventPublisher.publish(task.getTopic(), task.getMessage());
            // 更新数据库记录，task 任务表
            taskDao.updateTaskSendMessageCompleted(task);
            log.info("调整账户积分记录，发送MQ消息完成 userId: {} orderId:{} topic: {}", userId, creditOrderEntity.getOrderId(), task.getTopic());
        } catch (Exception e) {
            log.error("调整账户积分记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }

    }
}
