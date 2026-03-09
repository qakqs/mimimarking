package cn.bugstack.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "成功"),
    UN_ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    STRATEGY_RULE_WEIGHT_IS_NULL("ERR_BIZ_001", "业务异常，策略规则中 rule_weight 权重规则已适用但未配置"),
    STRATEGY_RULE_LUCK_AWARD_IS_NULL("ERR_BIZ_001", "业务异常，策略规则中 奖品未配置"),
    NEXT_NODE_ERROR("003", "决策树引擎计算失败"),
    NEXT_NODE_ERROR_NODE_NULL("004", "决策树引擎计算失败未找到可执行节点"),
    CREATE_RAFFLE_ACTIVITY_ORDER_ERROR("005", "以sku创建抽奖活动订单失败")
    ;

    private String code;
    private String info;

}
