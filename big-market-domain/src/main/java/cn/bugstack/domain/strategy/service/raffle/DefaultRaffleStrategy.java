package cn.bugstack.domain.strategy.service.raffle;

import cn.bugstack.domain.strategy.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.strategy.model.vo.*;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.IRaffleAward;
import cn.bugstack.domain.strategy.service.IRaffleStrategy;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.strategy.service.rule.chain.ILogicChain;
import cn.bugstack.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.bugstack.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import cn.bugstack.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.bugstack.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import cn.bugstack.types.common.Constants;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleAward {

    public DefaultRaffleStrategy(IStrategyRepository strategyRepository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory, DefaultLogicFactory defaultLogicFactory) {
        super(strategyRepository, strategyDispatch, defaultChainFactory, defaultTreeFactory, defaultLogicFactory);
    }

    @Override
    public StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);

        return logicChain.logic(userId, strategyId);
    }

    @Override
    public StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModel = strategyRepository.queryStrategyAwardRuleModel(strategyId, awardId);
        if (null == strategyAwardRuleModel) {
            return StrategyAwardVO.builder()
                    .awardId(awardId)
                    .build();
        }
        RuleTreeVO ruleTreeVO = strategyRepository.queryRuleTreeVOByTreeId(strategyAwardRuleModel.getRuleModels());
        if (null == ruleTreeVO) {
            throw new AppException(ResponseCode.NEXT_NODE_ERROR_NODE_NULL);
        }
        IDecisionTreeEngine decisionTreeEngine = defaultTreeFactory.openDecisionTreeEngine(ruleTreeVO);
        StrategyAwardData res = decisionTreeEngine.process(userId, strategyId, awardId);
        return StrategyAwardVO.builder()
                .logicModel(res.getAwardRuleValue())
                .awardId(res.getAwardId())
                .build();
    }


    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return strategyRepository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        strategyRepository.updateStrategyAwardStock(strategyId, awardId);

    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId) {
    // 优先从缓存获取
    String cacheKey = Constants.STRATEGY_AWARD_LIST_KEY(strategyId);
    }
}
