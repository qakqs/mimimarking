package cn.bugstack.domain.admin.service;

import cn.bugstack.domain.admin.model.aggregate.AdminRuleTreeAggregate;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeLineEntity;

import java.util.List;

/**
 * 后台规则树管理服务接口
 */
public interface IAdminRuleTreeService {

    void create(AdminRuleTreeEntity entity);

    void update(AdminRuleTreeEntity entity);

    void delete(String treeId);

    AdminRuleTreeAggregate detail(String treeId);

    List<AdminRuleTreeEntity> list(int page, int pageSize, String treeName);

    int count(String treeName);

    void saveNode(AdminRuleTreeNodeEntity entity);

    void deleteNode(Long id);

    void saveLine(AdminRuleTreeNodeLineEntity entity);

    void deleteLine(Long id);

}
