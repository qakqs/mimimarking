package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IRuleTreeDao {

    RuleTree queryRuleTreeByTreeId(String treeId);

    int insert(RuleTree po);

    int update(RuleTree po);

    int deleteByTreeId(@Param("treeId") String treeId);

    List<RuleTree> queryRuleTreePage(@Param("offset") int offset, @Param("limit") int limit,
                                     @Param("treeName") String treeName);

    int countRuleTree(@Param("treeName") String treeName);

}
