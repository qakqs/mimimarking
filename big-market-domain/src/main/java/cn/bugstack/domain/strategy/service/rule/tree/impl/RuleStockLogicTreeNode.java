package cn.bugstack.domain.strategy.service.rule.tree.impl;

import cn.bugstack.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.bugstack.domain.strategy.model.vo.TreeActionEntity;
import cn.bugstack.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.springframework.stereotype.Component;

/**
 * 库存节点
 */
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {
    @Override
    public TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {

        return TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
