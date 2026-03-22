package cn.bugstack.domain.activity.model.valobj;

import cn.bugstack.domain.activity.service.quota.policy.ITradePolicy;
import cn.bugstack.domain.activity.service.quota.policy.impl.CreditPayTradePay;
import cn.bugstack.domain.activity.service.quota.policy.impl.RebateNoPayPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderTradeTypeVO {
    credit_pay_trade("credit_pay_trade", "积分兑换，需要支付类交易", CreditPayTradePay.class),
    rebate_no_pay_trade("rebate_no_pay_trade", "返利奖品，不需要支付类交易", RebateNoPayPolicy.class),
    ;

    private final String code;
    private final String desc;
    private final Class<? extends ITradePolicy> tradePolicy;

}
