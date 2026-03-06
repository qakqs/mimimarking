package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RuleTreeNodeLine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper

public interface IRuleTreeNodeLineDao {
    public List<RuleTreeNodeLine> queryRuleTreeNodeLineListByTreeId(String treeId);
}
