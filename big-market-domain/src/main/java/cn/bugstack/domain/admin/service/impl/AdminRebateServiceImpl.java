package cn.bugstack.domain.admin.service.impl;

import cn.bugstack.domain.admin.model.entity.AdminDailyBehaviorRebateEntity;
import cn.bugstack.domain.admin.repository.IAdminRebateRepository;
import cn.bugstack.domain.admin.service.IAdminRebateService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminRebateServiceImpl implements IAdminRebateService {

    private final IAdminRebateRepository adminRebateRepository;

    public AdminRebateServiceImpl(IAdminRebateRepository adminRebateRepository) {
        this.adminRebateRepository = adminRebateRepository;
    }

    @Override
    public void save(AdminDailyBehaviorRebateEntity entity) {
        adminRebateRepository.saveRebateConfig(entity);
    }

    @Override
    public void delete(Long id) {
        adminRebateRepository.deleteRebateConfig(id);
    }

    @Override
    public void toggle(Long id, Integer state) {
        adminRebateRepository.toggleRebateConfig(id, state);
    }

    @Override
    public List<AdminDailyBehaviorRebateEntity> list(int page, int pageSize, String behaviorType, String state) {
        int offset = (page - 1) * pageSize;
        return adminRebateRepository.queryRebateConfigPage(offset, pageSize, behaviorType, state);
    }

    @Override
    public int count(String behaviorType, String state) {
        return adminRebateRepository.countRebateConfig(behaviorType, state);
    }

}
