package cn.bugstack.domain.strategy.service.rule.tree.factory.engine;


import cn.bugstack.domain.strategy.model.entity.StrategyAwardData;

import java.util.Date;

public interface IDecisionTreeEngine {

    StrategyAwardData process(String userId, Long strategyId, Integer awardId, Date endDateTime);
}
