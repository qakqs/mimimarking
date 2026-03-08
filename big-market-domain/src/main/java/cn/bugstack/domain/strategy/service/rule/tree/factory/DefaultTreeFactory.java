package cn.bugstack.domain.strategy.service.rule.tree.factory;


import cn.bugstack.types.vo.RuleTreeVO;
import cn.bugstack.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.bugstack.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import cn.bugstack.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 规则数工厂（如何调用）
 */
@Component
public class DefaultTreeFactory {

    private final Map<String, ILogicTreeNode> logicTreeNodeMap;


    public DefaultTreeFactory(Map<String, ILogicTreeNode> logicTreeNodeMap) {
        this.logicTreeNodeMap = logicTreeNodeMap;
    }

    public IDecisionTreeEngine openDecisionTreeEngine(RuleTreeVO ruleTreeVO) {
        return new DecisionTreeEngine(logicTreeNodeMap, ruleTreeVO);

    }


    public Map<String, ILogicTreeNode> getLogicTreeNodeMap() {
        return logicTreeNodeMap;
    }


}
