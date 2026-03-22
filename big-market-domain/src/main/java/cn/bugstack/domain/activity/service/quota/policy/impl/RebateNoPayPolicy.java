package cn.bugstack.domain.activity.service.quota.policy.impl;

import cn.bugstack.domain.activity.model.aggreate.CreateQuotaOrderAggregate;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.domain.activity.service.quota.policy.ITradePolicy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static cn.bugstack.domain.activity.model.valobj.OrderStateVO.completed;

@Component
@Slf4j
public class RebateNoPayPolicy implements ITradePolicy {

    @Resource
    private IActivityRepository activityRepository;
    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.getActivityOrderEntity().setState(completed);
        createQuotaOrderAggregate.getActivityOrderEntity().setPayAmount(BigDecimal.ZERO);
        activityRepository.doSaveCreditNoPayOrder(createQuotaOrderAggregate);

    }
}
