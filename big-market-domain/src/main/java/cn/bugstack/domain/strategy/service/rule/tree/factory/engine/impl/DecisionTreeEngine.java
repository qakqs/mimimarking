package cn.bugstack.domain.strategy.service.rule.tree.factory.engine.impl;

import cn.bugstack.domain.strategy.model.valobj.LogicTreeNodeVO;
import cn.bugstack.domain.strategy.model.entity.StrategyAwardData;
import cn.bugstack.domain.strategy.model.entity.TreeActionEntity;
import cn.bugstack.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import cn.bugstack.domain.strategy.model.valobj.RuleTreeNodeVO;
import cn.bugstack.domain.strategy.model.valobj.RuleTreeVO;
import cn.bugstack.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.bugstack.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import cn.bugstack.enums.RuleLogicCheckTypeVO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class DecisionTreeEngine implements IDecisionTreeEngine {

    private final Map<String, ILogicTreeNode> logicTreeNodeHashMap;

    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> logicTreeNodeHashMap, RuleTreeVO ruleTreeVO) {
        this.logicTreeNodeHashMap = logicTreeNodeHashMap;
        this.ruleTreeVO = ruleTreeVO;
    }

    @Override
    public StrategyAwardData process(String userId, Long strategyId, Integer awardId) {
        StrategyAwardData strategyAwardData = null;

        // 获取基础信息
        String nextNode = ruleTreeVO.getTreeRootRuleNode();
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();

        // 获取起始节点「根节点记录了第一个要执行的规则」
        RuleTreeNodeVO ruleTreeNode = treeNodeMap.get(nextNode);
        while (null != nextNode) {
            // 获取决策节点
            ILogicTreeNode logicTreeNode = logicTreeNodeHashMap.get(ruleTreeNode.getRuleKey());

            // 决策节点计算

            TreeActionEntity logicEntity = logicTreeNode.logic(LogicTreeNodeVO
                    .builder()
                    .awardId(awardId)
                    .userId(userId)
                    .strategyId(strategyId)
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .build()
            );
            RuleLogicCheckTypeVO ruleLogicCheckTypeVO = logicEntity.getRuleLogicCheckType();
            strategyAwardData = logicEntity.getStrategyAwardData();
            log.info("决策树引擎【{}】treeId:{} node:{} code:{}", ruleTreeVO.getTreeName(), ruleTreeVO.getTreeId(), nextNode, ruleLogicCheckTypeVO.getCode());

            // 获取下个节点
            nextNode = nextNode(ruleLogicCheckTypeVO.getCode(), ruleTreeNode.getTreeNodeLineVOList());
            ruleTreeNode = treeNodeMap.get(nextNode);
        }

        // 返回最终结果
        return strategyAwardData;
    }

    private String nextNode(String matterValue, List<RuleTreeNodeLineVO> ruleTreeNodeLineList) {
        if (null == ruleTreeNodeLineList || ruleTreeNodeLineList.isEmpty()) {
            return null;
        }
        for (RuleTreeNodeLineVO ruleTreeNodeLineVO : ruleTreeNodeLineList) {
            if (decisionLogic(matterValue, ruleTreeNodeLineVO)) {
                return ruleTreeNodeLineVO.getRuleNodeTo();
            }
        }
         // throw  new AppException(NEXT_NODE_ERROR_NODE_NULL);

        return null;
    }


    public boolean decisionLogic(String matterValue, RuleTreeNodeLineVO nodeLine) {
        return switch (nodeLine.getRuleLimitType()) {
            case EQUAL -> matterValue.equals(nodeLine.getRuleLimitValue().getCode());
            case GE, LE, LT, GT -> false;
            default -> false;
        };
    }

}
