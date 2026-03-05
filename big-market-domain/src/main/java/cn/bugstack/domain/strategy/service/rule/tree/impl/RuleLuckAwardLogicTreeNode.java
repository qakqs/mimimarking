package cn.bugstack.domain.strategy.service.rule.tree.impl;

import cn.bugstack.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.bugstack.domain.strategy.model.vo.StrategyAwardData;
import cn.bugstack.domain.strategy.model.vo.TreeActionEntity;
import cn.bugstack.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.springframework.stereotype.Component;

@Component("rule_luck_award")
public class RuleLuckAwardLogicTreeNode implements ILogicTreeNode {
    @Override
    public TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        return TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .strategyAwardData(StrategyAwardData.builder()
                        .awardId(101)
                        .awardRuleValue("1,100")
                        .build())
                .build();
    }
}
