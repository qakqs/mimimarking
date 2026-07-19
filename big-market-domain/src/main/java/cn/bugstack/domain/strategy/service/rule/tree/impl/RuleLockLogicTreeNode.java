package cn.bugstack.domain.strategy.service.rule.tree.impl;

import cn.bugstack.domain.strategy.model.valobj.LogicTreeNodeVO;
import cn.bugstack.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.bugstack.domain.strategy.model.entity.TreeActionEntity;
import cn.bugstack.domain.strategy.model.valobj.RuleTreeNodeEnum;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.types.exception.AppException;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.springframework.stereotype.Component;

/**
 * 次数锁节点
 */
@Component
public class RuleLockLogicTreeNode implements ILogicTreeNode {
    private static final Log log = Log.get(RuleLockLogicTreeNode.class);

    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public TreeActionEntity logic(LogicTreeNodeVO logicTreeNodeVO) {

        log.info("规则过滤 次数锁 logicTreeNodeVO:{}", logicTreeNodeVO);

        long raffleCount = 0L;
        try {
            raffleCount = Long.parseLong(logicTreeNodeVO.getRuleValue());
        } catch (Exception e) {
            log.error("转换失败", e);
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER);
        }
        Integer userRaffleCount = strategyRepository.queryTodayUserRaffleCount(logicTreeNodeVO.getUserId(), logicTreeNodeVO.getStrategyId());
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
