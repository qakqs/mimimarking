package cn.bugstack.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("SUCCESS", "成功"),
    UN_ERROR("UN_ERROR", "未知失败"),
    ILLEGAL_PARAMETER("ILLEGAL_PARAMETER", "非法参数"),
    STRATEGY_RULE_WEIGHT_IS_NULL("STRATEGY_RULE_WEIGHT_IS_NULL", "业务异常，策略规则中 rule_weight 权重规则已适用但未配置"),
    STRATEGY_RULE_LUCK_AWARD_IS_NULL("STRATEGY_RULE_LUCK_AWARD_IS_NULL", "业务异常，策略规则中 奖品未配置"),
    NEXT_NODE_ERROR("NEXT_NODE_ERROR", "决策树引擎计算失败"),
    NEXT_NODE_ERROR_NODE_NULL("NEXT_NODE_ERROR_NODE_NULL", "决策树引擎计算失败未找到可执行节点"),
    CREATE_RAFFLE_ACTIVITY_ORDER_ERROR("CREATE_RAFFLE_ACTIVITY_ORDER_ERROR", "以sku创建抽奖活动订单失败"),
    ACTIVITY_STATE_ERROR("ACTIVITY_STATE_ERROR", "活动状态异常"),
    ACTIVITY_DATE_ERROR("ACTIVITY_DATE_ERROR", "活动时间异常"),
    ACTIVITY_SKU_STOCK_ERROR("ACTIVITY_SKU_STOCK_ERROR", "sku库存扣减异常"),
    ACTIVITY_NULL_ERROR("ACTIVITY_NULL_ERROR", "没有该活动"),
    ACTIVITY_COUNT_ZERO_ERROR("ACTIVITY_COUNT_ZERO_ERROR", "该活动物剩余次数"),
    AWARD_INDEX_DUP("AWARD_INDEX_DUP", "该活动物剩余次数"),
    AWARD_ORDER_USED("AWARD_ORDER_USED", "改活动单已使用"),
    ;

    private String code;
    private String info;

}
