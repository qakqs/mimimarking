package cn.bugstack.domain.strategy.service.rule.impl;

import cn.bugstack.domain.strategy.model.entity.RuleActionEntity;
import cn.bugstack.domain.strategy.model.entity.RuleMatterEntity;
import cn.bugstack.domain.strategy.model.vo.LogicModel;
import cn.bugstack.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.IRaffleStrategy;
import cn.bugstack.domain.strategy.service.annoation.LogicStrategy;
import cn.bugstack.domain.strategy.service.rule.ILogicFilter;
import cn.bugstack.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@LogicStrategy(logicMode= LogicModel.RULE_BLACKLIST)
public class RuleBackListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {
    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        String ruleValue= strategyRepository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),
                ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId= Integer.valueOf(splitRuleValue[0]);
        String[] userBlackIdS = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIdS) {
            if (ruleMatterEntity.getUserId().equals(userBlackId)){
                RuleActionEntity<RuleActionEntity.RaffleBeforeEntity>ruleActionEntity = new RuleActionEntity<>();
                ruleActionEntity.setRuleModel(LogicModel.RULE_BLACKLIST.getCode());
                RuleActionEntity.RaffleBeforeEntity  raffleBeforeEntity = new RuleActionEntity.RaffleBeforeEntity();
                raffleBeforeEntity.setStrategyId(ruleMatterEntity.getStrategyId());
                raffleBeforeEntity.setAwardId(awardId);
                ruleActionEntity.setData(raffleBeforeEntity);
                ruleActionEntity.setCode(RuleLogicCheckTypeVO.TAKE_OVER.getCode());
                ruleActionEntity.setInfo(RuleLogicCheckTypeVO.TAKE_OVER.getInfo());
            }
        }
        return null;
    }
}
