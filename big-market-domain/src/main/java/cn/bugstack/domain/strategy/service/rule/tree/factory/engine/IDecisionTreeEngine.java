package cn.bugstack.domain.strategy.service.rule.tree.factory.engine;


import cn.bugstack.types.vo.StrategyAwardData;

public interface IDecisionTreeEngine {

    StrategyAwardData process(String userId, Long strategyId, Integer awardId);
}
