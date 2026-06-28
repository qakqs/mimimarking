package cn.bugstack.domain.activity.service.quota.policy.impl;

import cn.bugstack.domain.activity.model.aggreate.CreateQuotaOrderAggregate;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.domain.activity.service.quota.policy.ITradePolicy;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.springframework.stereotype.Component;

import static cn.bugstack.domain.activity.model.valobj.OrderStateVO.wait_pay;

@Component
public class CreditPayTradePay implements ITradePolicy {
    private static final Log log = Log.get(CreditPayTradePay.class);

    @Resource
    private IActivityRepository activityRepository;

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.getActivityOrderEntity().setState(wait_pay);
        activityRepository.doSaveCreditPayOrder(createQuotaOrderAggregate);

    }
}
