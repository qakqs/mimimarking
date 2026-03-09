package cn.bugstack.domain.strategy.service.rule.tree.impl;

import cn.bugstack.infrastructure.persistent.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.bugstack.types.vo.LogicTreeNodeVO;
import cn.bugstack.types.enums.RuleLogicCheckTypeVO;
import cn.bugstack.types.vo.StrategyAwardData;
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
    public cn.bugstack.types.vo.TreeActionEntity logic(LogicTreeNodeVO logicTreeNodeVO) {
        log.info("规则过滤 库存扣减 userid:{} strategyId:{} awardId:{}", logicTreeNodeVO.getUserId()
                , logicTreeNodeVO.getStrategyId(), logicTreeNodeVO.getAwardId());

        // 扣减库存
        Boolean status = strategyDispatch.submitStrategyAwardStock(logicTreeNodeVO.getStrategyId()
                ,logicTreeNodeVO.getAwardId()
        );
        if (status) {
            strategyRepository.awardSockConsumeSendQueue(cn.bugstack.types.vo.StrategyAwardStockKeyVO
                    .builder()
                    .awardId(logicTreeNodeVO.getAwardId())
                    .strategyId(logicTreeNodeVO.getStrategyId())
                    .build()
            );
            return cn.bugstack.types.vo.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                    .strategyAwardData(StrategyAwardData.builder()
                            .awardId(logicTreeNodeVO.getAwardId())
                            .awardRuleValue("")
                            .build())
                    .build();

        }

        return cn.bugstack.types.vo.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
