package cn.bugstack.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则物料实体信息，过滤规则必要参数信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RuleMatterEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 抽奖奖品id
     */
    private Integer awardId;
    /**
     *  抽奖规则类型 【rule_random】
     */
    private String ruleModel;

}
