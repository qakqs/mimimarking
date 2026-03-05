package cn.bugstack.domain.strategy.service.rule.tree;

import cn.bugstack.domain.strategy.model.vo.TreeActionEntity;

public interface ILogicTreeNode {

    TreeActionEntity logic(String userId, Long strategyId, Integer awardId);
}
