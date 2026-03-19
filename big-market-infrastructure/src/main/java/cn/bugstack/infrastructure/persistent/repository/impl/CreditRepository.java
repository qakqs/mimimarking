package cn.bugstack.infrastructure.persistent.repository.impl;

import cn.bugstack.domain.credit.model.aggregate.TradeAggregate;
import cn.bugstack.domain.credit.model.entity.CreditAccountEntity;
import cn.bugstack.domain.credit.model.entity.CreditOrderEntity;
import cn.bugstack.domain.credit.repository.ICreditRepository;
import cn.bugstack.infrastructure.persistent.dao.IUserCreditAccountDao;
import cn.bugstack.infrastructure.persistent.dao.IUserCreditOrderDao;
import cn.bugstack.infrastructure.persistent.po.UserCreditAccount;
import cn.bugstack.infrastructure.persistent.po.UserCreditOrder;
import cn.bugstack.infrastructure.persistent.redis.IRedisService;
import cn.bugstack.infrastructure.util.CreditConvert;
import cn.bugstack.types.common.Constants;
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

    @Override
    public void saveUserCreditTradeOrder(TradeAggregate tradeAggregate) {
        String userId = tradeAggregate.getUserId();
        CreditAccountEntity creditAccountEntity = tradeAggregate.getCreditAccountEntity();
        CreditOrderEntity creditOrderEntity = tradeAggregate.getCreditOrderEntity();

        UserCreditAccount userCreditAccount = convert.creditAccountConvert(creditAccountEntity);

        UserCreditOrder userCreditOrder = convert.userCreditOrderConvert(creditOrderEntity);

        RLock lock = redisService.getLock(USER_CREDIT_ACCOUNT_LOCK(userId, creditOrderEntity.getOutBusinessNo()));

        try {
            lock.lock(3, TimeUnit.SECONDS);
            transactionTemplate.execute(status -> {
                try {
                    // 1. 保存账户积分
                    UserCreditAccount creditAccount = userCreditAccountDao.queryUserCreditAccount(userCreditAccount);
                    if (null == userCreditAccount) {
                        userCreditAccountDao.insert(userCreditAccount);
                    } else {
                        userCreditAccountDao.updateUserCreditAccount(userCreditAccount);
                    }
                    // 2. 保存账户订单
                    userCreditOrderDao.insert(userCreditOrder);

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
    }
}
