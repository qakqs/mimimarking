package cn.bugstack.domain.admin.service;

import cn.bugstack.domain.admin.model.entity.AdminDailyBehaviorRebateEntity;

import java.util.List;

/**
 * 后台返利配置管理服务接口
 */
public interface IAdminRebateService {

    void save(AdminDailyBehaviorRebateEntity entity);

    void delete(Long id);

    void toggle(Long id, Integer state);

    List<AdminDailyBehaviorRebateEntity> list(int page, int pageSize, String behaviorType, String state);

    int count(String behaviorType, String state);

}
