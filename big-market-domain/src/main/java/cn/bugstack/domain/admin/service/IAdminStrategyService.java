package cn.bugstack.domain.admin.service;

import cn.bugstack.domain.admin.model.entity.AdminRuleTreeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeLineEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyAwardEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyRuleEntity;

import java.util.List;

/**
 * 后台策略管理服务接口（含规则树管理）
 */
public interface IAdminStrategyService {

    // ====== Strategy ======

    void createStrategy(AdminStrategyEntity entity);


    void update(AdminStrategyEntity entity);

    void delete(Long strategyId);

    AdminStrategyEntity detail(Long strategyId);

    List<AdminStrategyEntity> list(int page, int pageSize, String strategyDesc);

    int count(String strategyDesc);

    // ====== Strategy Award ======

    List<AdminStrategyAwardEntity> awardList(Long strategyId);

    void saveAward(AdminStrategyAwardEntity entity);

    void deleteAward(Long id);

    // ====== Strategy Rule ======

    List<AdminStrategyRuleEntity> ruleList(Long strategyId);

    void saveRule(AdminStrategyRuleEntity entity);

    void deleteRule(Long id);

    // ====== Rule Tree ======

    void createRuleTree(AdminRuleTreeEntity entity);

    void updateRuleTree(AdminRuleTreeEntity entity);

    void deleteRuleTree(String treeId);

    List<AdminRuleTreeEntity> listRuleTree(int page, int pageSize, String treeName);

    int countRuleTree(String treeName);

    void saveRuleTreeNode(AdminRuleTreeNodeEntity entity);

    void deleteRuleTreeNode(Long id);

    void saveRuleTreeNodeLine(AdminRuleTreeNodeLineEntity entity);

    void deleteRuleTreeNodeLine(Long id);

    Integer generateStrategyId();
}
