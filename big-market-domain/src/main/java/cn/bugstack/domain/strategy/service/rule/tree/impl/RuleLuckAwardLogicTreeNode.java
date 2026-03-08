package cn.bugstack.domain.strategy.service.rule.tree.impl;

import cn.bugstack.types.vo.LogicTreeNodeVO;
import cn.bugstack.types.vo.RuleLogicCheckTypeVO;
import cn.bugstack.types.vo.StrategyAwardData;
import cn.bugstack.types.vo.TreeActionEntity;
import cn.bugstack.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.bugstack.types.common.Constants;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("rule_luck_award")
public class RuleLuckAwardLogicTreeNode implements ILogicTreeNode {
    @Override
    public TreeActionEntity logic(LogicTreeNodeVO logicTreeNodeVO) {
        log.info("规则过滤 奖品兜底 userid:{} strategyId:{} awardId:{}", logicTreeNodeVO.getUserId()
                , logicTreeNodeVO.getStrategyId(), logicTreeNodeVO.getAwardId());
        String[] split = logicTreeNodeVO.getRuleValue().split(Constants.COLON);
        if (split == null || split.length == 0) {
            log.error("规则过滤 奖品兜底 奖品未配置 userid:{} strategyId:{} awardId:{}", logicTreeNodeVO.getUserId()
                    , logicTreeNodeVO.getStrategyId(), logicTreeNodeVO.getAwardId());
            throw new AppException(ResponseCode.STRATEGY_RULE_LUCK_AWARD_IS_NULL);
        }
        Integer luckAwardId = Integer.valueOf(split[0]);
        String awardRuleValue = split.length > 1 ? split[1] : "";

        log.info("规则过滤 奖品兜底 userid:{} strategyId:{} awardId:{} awardRuleValue:{}", logicTreeNodeVO.getUserId()
                , logicTreeNodeVO.getStrategyId(), logicTreeNodeVO.getAwardId(), awardRuleValue);

        return TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .strategyAwardData(StrategyAwardData.builder()
                        .awardId(luckAwardId)
                        .awardRuleValue(awardRuleValue)
                        .build())
                .build();
    }
}
