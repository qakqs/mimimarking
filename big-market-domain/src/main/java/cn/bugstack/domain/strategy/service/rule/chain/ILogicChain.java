package cn.bugstack.domain.strategy.service.rule.chain;

import cn.bugstack.domain.strategy.model.vo.StrategyAwardVO;

public interface ILogicChain extends ILogicChainArmory {

    StrategyAwardVO logic(String userId, Long strategyId);

}
