package cn.bugstack.domain.strategy.service.rule.impl;

import cn.bugstack.domain.strategy.model.entity.RuleActionEntity;
import cn.bugstack.domain.strategy.model.entity.RuleMatterEntity;
import cn.bugstack.domain.strategy.model.vo.LogicModel;
import cn.bugstack.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.annoation.LogicStrategy;
import cn.bugstack.domain.strategy.service.rule.ILogicFilter;
import cn.bugstack.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@LogicStrategy(logicMode = LogicModel.RULE_WIGHT)

public class RuleWeightLogicFiler implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository strategyRepository;

    /**
     *
     */
    private Long userSource = 4096L;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        String userId = ruleMatterEntity.getUserId();
        Long strategyId = ruleMatterEntity.getStrategyId();
        String ruleValues = strategyRepository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),
                ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(ruleValues);

        if (null == analyticalValueGroup || analyticalValueGroup.isEmpty()) {
            RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> res = new RuleActionEntity<>();
            res.setCode(RuleLogicCheckTypeVO.ALLOW.getCode());
            res.setInfo(RuleLogicCheckTypeVO.ALLOW.getInfo());
            return res;
        }
        ArrayList<Long> analyticalValueGroupKeyList = new ArrayList<>(analyticalValueGroup.keySet());
        Collections.sort(analyticalValueGroupKeyList);
        Long nextValue = analyticalValueGroupKeyList.stream().filter(key -> userSource >= key)
                .findFirst().orElse(null);
        if (null != nextValue) {
            RuleActionEntity<RuleActionEntity.RaffleBeforeEntity>  res = new RuleActionEntity<>();
            res.setCode(RuleLogicCheckTypeVO.TAKE_OVER.getCode());
            res.setInfo(RuleLogicCheckTypeVO.TAKE_OVER.getInfo());
            RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = new RuleActionEntity.RaffleBeforeEntity();
            raffleBeforeEntity.setStrategyId(strategyId);
            raffleBeforeEntity.setRuleWeightValueKey(analyticalValueGroup.get(nextValue));
            res.setData(raffleBeforeEntity);
            return res;
        }
        return null;
    }

    private Map<Long, String> getAnalyticalValue(String ruleValues) {
        Map<Long, String> ruleValueMap = new HashMap<>();
        String[] ruleValuesGroups = ruleValues.split(Constants.SPACE);
        if (null != ruleValuesGroups && ruleValuesGroups.length > 0) {
            return ruleValueMap;
        }
        for (String ruleValuesGroup : ruleValuesGroups) {
            String[] split = ruleValuesGroup.split(Constants.COLON);
            if (split.length != 2) {
                throw new IllegalArgumentException("ruleValuesGroup must contain exactly 2 values" + ruleValuesGroup);
            }
            ruleValueMap.put(Long.parseLong(split[0]), ruleValuesGroup);
        }
        return ruleValueMap;
    }

}
