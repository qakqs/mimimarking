package cn.bugstack.domain.admin.repository;

import cn.bugstack.domain.admin.model.entity.AdminActivityCountEntity;
import cn.bugstack.domain.admin.model.entity.AdminActivityEntity;
import cn.bugstack.domain.admin.model.entity.AdminActivitySkuEntity;

import java.util.List;

/**
 * 后台管理 - 活动仓储接口
 */
public interface IAdminActivityRepository {

    void saveActivity(AdminActivityEntity entity);

    void updateActivity(AdminActivityEntity entity);

    void deleteActivity(Long activityId);

    AdminActivityEntity queryActivityById(Long activityId);

    List<AdminActivityEntity> queryActivityPage(int offset, int limit, String activityName, String state);

    int countActivity(String activityName, String state);

    void toggleActivityStatus(Long activityId, Integer state);

    void saveActivitySku(AdminActivitySkuEntity entity);

    void deleteActivitySku(Long sku);

    List<AdminActivitySkuEntity> querySkuListByActivityId(Long activityId);

    void adjustSkuStock(Long sku, Integer delta);

    void saveActivityCount(AdminActivityCountEntity entity);

    AdminActivityCountEntity queryActivityCountByActivityId(Long activityId);

}
