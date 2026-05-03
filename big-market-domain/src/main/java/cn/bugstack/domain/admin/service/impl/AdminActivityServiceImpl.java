package cn.bugstack.domain.admin.service.impl;

import cn.bugstack.domain.admin.model.aggregate.AdminActivityAggregate;
import cn.bugstack.domain.admin.model.entity.AdminActivityCountEntity;
import cn.bugstack.domain.admin.model.entity.AdminActivityEntity;
import cn.bugstack.domain.admin.model.entity.AdminActivitySkuEntity;
import cn.bugstack.domain.admin.repository.IAdminActivityRepository;
import cn.bugstack.domain.admin.service.IAdminActivityService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminActivityServiceImpl implements IAdminActivityService {

    private final IAdminActivityRepository adminActivityRepository;

    public AdminActivityServiceImpl(IAdminActivityRepository adminActivityRepository) {
        this.adminActivityRepository = adminActivityRepository;
    }

    @Override
    public void create(AdminActivityAggregate aggregate) {
        adminActivityRepository.saveActivity(aggregate.getActivity());
        if (aggregate.getCount() != null) {
            adminActivityRepository.saveActivityCount(aggregate.getCount());
        }
    }

    @Override
    public void update(AdminActivityEntity entity) {
        adminActivityRepository.updateActivity(entity);
    }

    @Override
    public void delete(Long activityId) {
        adminActivityRepository.deleteActivity(activityId);
    }

    @Override
    public AdminActivityAggregate detail(Long activityId) {
        AdminActivityEntity activity = adminActivityRepository.queryActivityById(activityId);
        AdminActivityCountEntity count = adminActivityRepository.queryActivityCountByActivityId(activityId);
        return AdminActivityAggregate.builder().activity(activity).count(count).build();
    }

    @Override
    public List<AdminActivityEntity> list(int page, int pageSize, String activityName, String state) {
        int offset = (page - 1) * pageSize;
        return adminActivityRepository.queryActivityPage(offset, pageSize, activityName, state);
    }

    @Override
    public int count(String activityName, String state) {
        return adminActivityRepository.countActivity(activityName, state);
    }

    @Override
    public void toggleStatus(Long activityId, Integer state) {
        adminActivityRepository.toggleActivityStatus(activityId, state);
    }

    @Override
    public void saveCount(AdminActivityCountEntity entity) {
        adminActivityRepository.saveActivityCount(entity);
    }

    @Override
    public AdminActivityCountEntity getCount(Long activityId) {
        return adminActivityRepository.queryActivityCountByActivityId(activityId);
    }

    @Override
    public void saveSku(AdminActivitySkuEntity entity) {
        adminActivityRepository.saveActivitySku(entity);
    }

    @Override
    public void deleteSku(Long sku) {
        adminActivityRepository.deleteActivitySku(sku);
    }

    @Override
    public List<AdminActivitySkuEntity> skuList(Long activityId) {
        return adminActivityRepository.querySkuListByActivityId(activityId);
    }

    @Override
    public void adjustSkuStock(Long sku, Integer delta) {
        adminActivityRepository.adjustSkuStock(sku, delta);
    }

}
