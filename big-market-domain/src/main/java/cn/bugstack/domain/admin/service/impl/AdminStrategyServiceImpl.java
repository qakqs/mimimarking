package cn.bugstack.domain.admin.service.impl;

import cn.bugstack.domain.admin.model.entity.AdminStrategyAwardEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyRuleEntity;
import cn.bugstack.domain.admin.repository.IAdminStrategyRepository;
import cn.bugstack.domain.admin.service.IAdminStrategyService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminStrategyServiceImpl implements IAdminStrategyService {

    private final IAdminStrategyRepository adminStrategyRepository;

    public AdminStrategyServiceImpl(IAdminStrategyRepository adminStrategyRepository) {
        this.adminStrategyRepository = adminStrategyRepository;
    }

    @Override
    public void create(AdminStrategyEntity entity) {
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

}
