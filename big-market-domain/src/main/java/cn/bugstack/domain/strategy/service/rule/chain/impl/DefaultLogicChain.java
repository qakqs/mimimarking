package cn.bugstack.domain.strategy.service.rule.chain.impl;

import cn.bugstack.domain.strategy.model.valobj.StrategyAwardVO;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.strategy.model.valobj.LogicChainEnum;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultLogicChain extends AbstractLogicChain {
    private static final Log log = Log.get(DefaultLogicChain.class);
    @Resource
    private IStrategyDispatch strategyArmoryDispatch;

    @Override
    public StrategyAwardVO logic(String userId, Long strategyId) {
        Integer awardId = strategyArmoryDispatch.getRandomAwardId(strategyId);
        log.info("抽奖责任链-默认处理 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
        return StrategyAwardVO.builder().logicModel(ruleModel()).awardId(awardId).build();
    }

    @Override
    protected String ruleModel() {
        return LogicChainEnum.DEFAULT.getChainName();
    }
}
