package cn.bugstack.domain.strategy.service.rule.tree;

import cn.bugstack.domain.strategy.model.vo.LogicTreeNodeVO;
import cn.bugstack.domain.strategy.model.vo.TreeActionEntity;

public interface ILogicTreeNode {

    TreeActionEntity logic(LogicTreeNodeVO logicTreeNodeVO);
}
