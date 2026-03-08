package cn.bugstack.domain.strategy.service.rule.tree.impl;

import cn.bugstack.types.vo.LogicTreeNodeVO;
import cn.bugstack.types.vo.RuleLogicCheckTypeVO;
import cn.bugstack.types.vo.TreeActionEntity;
import cn.bugstack.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 次数锁节点
 */
@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {

    private Long userRaffleCount = 10L;


    @Override
    public TreeActionEntity logic(LogicTreeNodeVO logicTreeNodeVO) {
        log.info("规则过滤 次数锁 userid:{} strategyId:{} awardId:{}", logicTreeNodeVO.getUserId()
                , logicTreeNodeVO.getStrategyId(), logicTreeNodeVO.getAwardId());

        long raffleCount = 0L;
        try {
            raffleCount = Long.parseLong(logicTreeNodeVO.getRuleValue());
        } catch (Exception e) {
            log.error("转换失败", e);
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER);
        }

        if (userRaffleCount >= raffleCount) {
            // 放行（抽奖次数大于限定值）
            return TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                    .build();
        }
        // 接管 （抽奖次数小于限定值）
        return TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
