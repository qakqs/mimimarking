package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRuleTreeDao {
    public RuleTree queryRuleTreeByTreeId(String treeId);
}
