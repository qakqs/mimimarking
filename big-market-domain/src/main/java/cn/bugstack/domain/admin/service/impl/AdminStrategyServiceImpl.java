package cn.bugstack.domain.admin.service.impl;

import cn.bugstack.domain.admin.model.entity.AdminRuleTreeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeEntity;
import cn.bugstack.domain.admin.model.entity.AdminRuleTreeNodeLineEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyAwardEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyRuleEntity;
import cn.bugstack.domain.admin.repository.IAdminRuleTreeRepository;
import cn.bugstack.domain.admin.repository.IAdminStrategyRepository;
import cn.bugstack.domain.admin.service.IAdminStrategyService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminStrategyServiceImpl implements IAdminStrategyService {

    private final IAdminStrategyRepository adminStrategyRepository;
    private final IAdminRuleTreeRepository adminRuleTreeRepository;

    public AdminStrategyServiceImpl(IAdminStrategyRepository adminStrategyRepository,
                                    IAdminRuleTreeRepository adminRuleTreeRepository) {
        this.adminStrategyRepository = adminStrategyRepository;
        this.adminRuleTreeRepository = adminRuleTreeRepository;
    }

    // ====== Strategy ======

    @Override
    public void createStrategy(AdminStrategyEntity entity) {
        adminStrategyRepository.saveStrategy(entity);
    }

    @Override
    public void update(AdminStrategyEntity entity) {
        adminStrategyRepository.updateStrategy(entity);
    }

    @Override
    public void delete(Long strategyId) {
        adminStrategyRepository.deleteStrategy(strategyId);
    }

    @Override
    public AdminStrategyEntity detail(Long strategyId) {
        return adminStrategyRepository.queryStrategyById(strategyId);
    }

    @Override
    public List<AdminStrategyEntity> list(int page, int pageSize, String strategyDesc) {
        int offset = (page - 1) * pageSize;
        return adminStrategyRepository.queryStrategyPage(offset, pageSize, strategyDesc);
    }

    @Override
    public int count(String strategyDesc) {
        return adminStrategyRepository.countStrategy(strategyDesc);
    }

    // ====== Strategy Award ======

    @Override
    public List<AdminStrategyAwardEntity> awardList(Long strategyId) {
        return adminStrategyRepository.queryAwardListByStrategyId(strategyId);
    }

    @Override
    public void saveAward(AdminStrategyAwardEntity entity) {
        adminStrategyRepository.saveStrategyAward(entity);
    }

    @Override
    public void deleteAward(Long id) {
        adminStrategyRepository.deleteStrategyAward(id);
    }

    // ====== Strategy Rule ======

    @Override
    public List<AdminStrategyRuleEntity> ruleList(Long strategyId) {
        return adminStrategyRepository.queryRuleListByStrategyId(strategyId);
    }

    @Override
    public void saveRule(AdminStrategyRuleEntity entity) {
        adminStrategyRepository.saveStrategyRule(entity);
    }

    @Override
    public void deleteRule(Long id) {
        adminStrategyRepository.deleteStrategyRule(id);
    }

    // ====== Rule Tree ======

    @Override
    public void createRuleTree(AdminRuleTreeEntity entity) {
        adminRuleTreeRepository.saveRuleTree(entity);
    }

    @Override
    public void updateRuleTree(AdminRuleTreeEntity entity) {
        adminRuleTreeRepository.updateRuleTree(entity);
    }

    @Override
    public void deleteRuleTree(String treeId) {
        adminRuleTreeRepository.deleteRuleTree(treeId);
    }

    @Override
    public List<AdminRuleTreeEntity> listRuleTree(int page, int pageSize, String treeName) {
        int offset = (page - 1) * pageSize;
        return adminRuleTreeRepository.queryRuleTreePage(offset, pageSize, treeName);
    }

    @Override
    public int countRuleTree(String treeName) {
        return adminRuleTreeRepository.countRuleTree(treeName);
    }

    @Override
    public void saveRuleTreeNode(AdminRuleTreeNodeEntity entity) {
        adminRuleTreeRepository.saveRuleTreeNode(entity);
    }

    @Override
    public void deleteRuleTreeNode(Long id) {
        adminRuleTreeRepository.deleteRuleTreeNode(id);
    }

    @Override
    public void saveRuleTreeNodeLine(AdminRuleTreeNodeLineEntity entity) {
        adminRuleTreeRepository.saveRuleTreeNodeLine(entity);
    }

    @Override
    public void deleteRuleTreeNodeLine(Long id) {
        adminRuleTreeRepository.deleteRuleTreeNodeLine(id);
    }

    @Override
    public Integer generateStrategyId() {
        return adminStrategyRepository.generateStrategyId();
    }

}
