package cn.bugstack.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 决策树的节点，这些节点可以组合出任意需要的规则树。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RuleTreeNodeVO {
    /**
     * 规则树ID
     */
    private String treeId;
    /**
     * 规则Key
     */
    private String ruleKey;
    /**
     * 规则描述
     */
    private String ruleDesc;
    /**
     * 规则比值
     */
    private String ruleValue;

    /**
     * 规则连线
     */
    private List<RuleTreeNodeLineVO> treeNodeLineVOList;

}
