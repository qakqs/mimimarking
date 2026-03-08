package cn.bugstack.domain.strategy.service.rule.filter;


import cn.bugstack.types.entity.RuleActionEntity;
import cn.bugstack.types.entity.RuleMatterEntity;

public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {
    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
