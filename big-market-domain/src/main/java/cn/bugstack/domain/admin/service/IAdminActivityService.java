package cn.bugstack.domain.admin.service;

import cn.bugstack.domain.admin.model.aggregate.AdminActivityAggregate;
import cn.bugstack.domain.admin.model.entity.AdminActivityCountEntity;
import cn.bugstack.domain.admin.model.entity.AdminActivityEntity;
import cn.bugstack.domain.admin.model.entity.AdminActivitySkuEntity;

import java.util.List;

/**
 * 后台活动管理服务接口
 */
public interface IAdminActivityService {

    void createActivity(AdminActivityEntity activity);

    void update(AdminActivityEntity entity);

    void delete(Long activityId);

    AdminActivityAggregate detail(Long activityId);

    List<AdminActivityEntity> list(int page, int pageSize, String activityName, String state);

    int count(String activityName, String state);

    void toggleStatus(Long activityId, Integer state);

    void saveCount(AdminActivityCountEntity entity);

    AdminActivityCountEntity getCount(Long activityId);

    void saveSku(AdminActivitySkuEntity entity);

    void deleteSku(Long sku);

    List<AdminActivitySkuEntity> skuList(Long activityId);

    void adjustSkuStock(Long sku, Integer delta);

    Integer generateActivityId();
}
