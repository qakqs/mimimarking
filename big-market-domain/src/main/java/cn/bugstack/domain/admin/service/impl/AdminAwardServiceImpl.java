package cn.bugstack.domain.admin.service.impl;

import cn.bugstack.domain.admin.model.entity.AdminAwardEntity;
import cn.bugstack.domain.admin.repository.IAdminAwardRepository;
import cn.bugstack.domain.admin.service.IAdminAwardService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminAwardServiceImpl implements IAdminAwardService {

    private final IAdminAwardRepository adminAwardRepository;

    public AdminAwardServiceImpl(IAdminAwardRepository adminAwardRepository) {
        this.adminAwardRepository = adminAwardRepository;
    }

    @Override
    public void create(AdminAwardEntity entity) {
        adminAwardRepository.saveAward(entity);
    }

    @Override
    public void update(AdminAwardEntity entity) {
        adminAwardRepository.updateAward(entity);
    }

    @Override
    public void delete(Integer awardId) {
        adminAwardRepository.deleteAward(awardId);
    }

    @Override
    public AdminAwardEntity detail(Integer awardId) {
        return adminAwardRepository.queryAwardById(awardId);
    }

    @Override
    public List<AdminAwardEntity> list(int page, int pageSize, String awardDesc) {
        int offset = (page - 1) * pageSize;
        return adminAwardRepository.queryAwardPage(offset, pageSize, awardDesc);
    }

    @Override
    public int count(String awardDesc) {
        return adminAwardRepository.countAward(awardDesc);
    }

}
