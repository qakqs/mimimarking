package cn.bugstack.domain.admin.repository;

import cn.bugstack.domain.admin.model.entity.AdminRuleTreeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeLineEntity;

import java.util.List;

/**
 * 后台管理 - 规则树仓储接口
 */
public interface IAdminRuleTreeRepository {

    void saveRuleTree(AdminRuleTreeEntity entity);

    void updateRuleTree(AdminRuleTreeEntity entity);

    void deleteRuleTree(String treeId);

    AdminRuleTreeEntity queryRuleTreeById(String treeId);

    List<AdminRuleTreeEntity> queryRuleTreePage(int offset, int limit, String treeName);

    int countRuleTree(String treeName);

    void saveRuleTreeNode(AdminRuleTreeNodeEntity entity);

    void deleteRuleTreeNode(Long id);

    List<AdminRuleTreeNodeEntity> queryNodesByTreeId(String treeId);

    void saveRuleTreeNodeLine(AdminRuleTreeNodeLineEntity entity);

    void deleteRuleTreeNodeLine(Long id);

    List<AdminRuleTreeNodeLineEntity> queryLinesByTreeId(String treeId);

}
