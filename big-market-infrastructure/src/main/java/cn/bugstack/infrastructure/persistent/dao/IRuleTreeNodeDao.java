package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RuleTreeNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper

public interface IRuleTreeNodeDao {
    public List<RuleTreeNode> queryRuleTreeNodeListByTreeId(String treeId);
}
