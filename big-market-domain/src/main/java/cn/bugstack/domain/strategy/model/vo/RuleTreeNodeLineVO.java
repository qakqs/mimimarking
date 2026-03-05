package cn.bugstack.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 决策树节点连线，用于标识出怎么从一个节点到下一个节点。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RuleTreeNodeLineVO {

    /**
     * 规则树ID
     */
    private Integer treeId;
    /**
     * 规则Key节点 From
     */
    private String ruleNodeFrom;
    /**
     * 规则Key节点 To
     */
    private String ruleNodeTo;
    /**
     * 限定类型；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围]
     */
    private RuleLimitTypeVO ruleLimitType;
    /**
     * 限定值（到下个节点）
     */
    private RuleLogicCheckTypeVO ruleLimitValue;


}
