package cn.bugstack.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 决策树的树根信息，标记出最开始从哪个节点执行「treeRootRuleNode」。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeVO {


    /**
     * 规则数id
     */
    private String treeId;

    /**
     * 汇总数名称
     */
    private String treeName;

    /**
     * 规则数描述
     */
    private String treeDesc;

    /**
     * 规则根节点
     */
    private String treeRootRuleNode;

    /**
     * 规则连线
     */
    private Map<String, RuleTreeNodeVO> treeNodeMap;

}
