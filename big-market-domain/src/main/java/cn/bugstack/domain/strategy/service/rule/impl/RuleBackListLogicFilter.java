package cn.bugstack.domain.strategy.service.rule.impl;

import cn.bugstack.domain.strategy.model.entity.RuleActionEntity;
import cn.bugstack.domain.strategy.model.entity.RuleMatterEntity;
import cn.bugstack.domain.strategy.model.vo.LogicModel;
import cn.bugstack.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.annoation.LogicStrategy;
import cn.bugstack.domain.strategy.service.rule.ILogicFilter;
import cn.bugstack.types.common.Constants;
import cn.bugstack.types.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Component;

import static cn.bugstack.types.enums.ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL;


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
        if (StringUtils.isBlank(ruleValue)) {
             throw new AppException(STRATEGY_RULE_WEIGHT_IS_NULL);
        }

        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId= Integer.valueOf(splitRuleValue[0]);
        String[] userBlackIdS = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIdS) {
            if (ruleMatterEntity.getUserId().equals(userBlackId)){

                return getRaffleBeforeEntityRuleActionEntity(ruleMatterEntity, awardId);
            }
        }
        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }

    private RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> getRaffleBeforeEntityRuleActionEntity(RuleMatterEntity ruleMatterEntity, Integer awardId) {
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity>ruleActionEntity = new RuleActionEntity<>();
        ruleActionEntity.setRuleModel(LogicModel.RULE_BLACKLIST.getCode());
        RuleActionEntity.RaffleBeforeEntity  raffleBeforeEntity = new RuleActionEntity.RaffleBeforeEntity();
        raffleBeforeEntity.setStrategyId(ruleMatterEntity.getStrategyId());
        raffleBeforeEntity.setAwardId(awardId);
        ruleActionEntity.setData(raffleBeforeEntity);
        ruleActionEntity.setCode(RuleLogicCheckTypeVO.TAKE_OVER.getCode());
        ruleActionEntity.setInfo(RuleLogicCheckTypeVO.TAKE_OVER.getInfo());
        return ruleActionEntity;
    }
}
