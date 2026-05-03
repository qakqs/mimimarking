package cn.bugstack.domain.admin.repository;

import cn.bugstack.domain.admin.model.entity.AdminDailyBehaviorRebateEntity;

import java.util.List;

/**
 * 后台管理 - 返利配置仓储接口
 */
public interface IAdminRebateRepository {

    void saveRebateConfig(AdminDailyBehaviorRebateEntity entity);

    void deleteRebateConfig(Long id);

    void toggleRebateConfig(Long id, Integer state);

    List<AdminDailyBehaviorRebateEntity> queryRebateConfigPage(int offset, int limit, String behaviorType, String state);

    int countRebateConfig(String behaviorType, String state);

}
