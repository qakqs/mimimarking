package cn.bugstack.domain.strategy.service.raffle;

import cn.bugstack.domain.strategy.model.entity.StrategyAwardData;
import cn.bugstack.domain.strategy.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.strategy.model.valobj.StrategyAwardVO;
import cn.bugstack.domain.strategy.model.valobj.RuleTreeVO;
import cn.bugstack.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import cn.bugstack.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import cn.bugstack.domain.strategy.respository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.IRaffleAward;
import cn.bugstack.domain.strategy.service.IRaffleRule;
import cn.bugstack.domain.strategy.service.IRaffleStock;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.strategy.service.rule.chain.ILogicChain;
import cn.bugstack.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.bugstack.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.bugstack.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleAward, IRaffleStock, IRaffleRule {


    public DefaultRaffleStrategy(IStrategyRepository strategyRepository, IStrategyDispatch strategyDispatch,
                                 DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(strategyRepository, strategyDispatch, defaultChainFactory, defaultTreeFactory);
    }

    @Override
    public StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);

        return logicChain.logic(userId, strategyId);
    }

    @Override
    public StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        return raffleLogicTree(userId, strategyId, awardId, null);
    }

    @Override
    public StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId, Date endDateTime) {
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
        StrategyAwardData res = decisionTreeEngine.process(userId, strategyId, awardId, endDateTime);
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
        return strategyRepository.queryStrategyAwardList(strategyId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long activityId) {
        Long strategyId = strategyRepository.queryStrategyIdByActivityId(activityId);
        return this.queryRaffleStrategyAwardList(strategyId);
    }

    @Override
    public Map<String, Integer> queryAwardRuleLockCount(String... treeIds) {
        return strategyRepository.queryAwardRuleLockCount(treeIds);
    }
}
