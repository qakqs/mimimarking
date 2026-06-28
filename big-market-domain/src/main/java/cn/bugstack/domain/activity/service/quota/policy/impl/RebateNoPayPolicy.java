package cn.bugstack.domain.activity.service.quota.policy.impl;

import cn.bugstack.domain.activity.model.aggreate.CreateQuotaOrderAggregate;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.domain.activity.service.quota.policy.ITradePolicy;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static cn.bugstack.domain.activity.model.valobj.OrderStateVO.completed;

@Component
public class RebateNoPayPolicy implements ITradePolicy {
    private static final Log log = Log.get(RebateNoPayPolicy.class);

    @Resource
    private IActivityRepository activityRepository;
    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.getActivityOrderEntity().setState(completed);
        createQuotaOrderAggregate.getActivityOrderEntity().setPayAmount(BigDecimal.ZERO);
        activityRepository.doSaveCreditNoPayOrder(createQuotaOrderAggregate);

    }
}
