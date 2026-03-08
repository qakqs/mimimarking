package cn.bugstack.domain.strategy.service.rule.tree;

import cn.bugstack.types.vo.LogicTreeNodeVO;
import cn.bugstack.types.vo.TreeActionEntity;

public interface ILogicTreeNode {

    TreeActionEntity logic(LogicTreeNodeVO logicTreeNodeVO);
}
