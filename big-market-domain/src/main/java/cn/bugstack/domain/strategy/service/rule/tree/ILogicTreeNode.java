package cn.bugstack.domain.strategy.service.rule.tree;

import cn.bugstack.domain.strategy.model.valobj.LogicTreeNodeVO;
import cn.bugstack.domain.strategy.model.entity.TreeActionEntity;

public interface ILogicTreeNode {

    TreeActionEntity logic(LogicTreeNodeVO logicTreeNodeVO);
}
