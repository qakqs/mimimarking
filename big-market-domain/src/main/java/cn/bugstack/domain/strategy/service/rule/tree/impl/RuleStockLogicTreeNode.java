package cn.bugstack.domain.strategy.service.rule.tree.impl;

import cn.bugstack.domain.strategy.respository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.bugstack.domain.strategy.model.valobj.LogicTreeNodeVO;
import cn.bugstack.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.bugstack.domain.strategy.model.entity.StrategyAwardData;
import cn.bugstack.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import cn.bugstack.domain.strategy.model.entity.TreeActionEntity;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 库存节点
 */
@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public TreeActionEntity logic(LogicTreeNodeVO logicTreeNodeVO) {
        log.info("规则过滤 库存扣减 userid:{} strategyId:{} awardId:{}", logicTreeNodeVO.getUserId()
                , logicTreeNodeVO.getStrategyId(), logicTreeNodeVO.getAwardId());

        // 扣减库存
        Boolean status = strategyDispatch.submitStrategyAwardStock(logicTreeNodeVO.getStrategyId()
                ,logicTreeNodeVO.getAwardId(), logicTreeNodeVO.getEndDateTime()
        );
        if (status) {
            strategyRepository.awardSockConsumeSendQueue(StrategyAwardStockKeyVO
                    .builder()
                    .awardId(logicTreeNodeVO.getAwardId())
                    .strategyId(logicTreeNodeVO.getStrategyId())
                    .build()
            );
            return TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                    .strategyAwardData(StrategyAwardData.builder()
                            .awardId(logicTreeNodeVO.getAwardId())
                            .awardRuleValue("")
                            .build())
                    .build();

        }

        return TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
