package cn.bugstack.domain.admin.repository;

import cn.bugstack.domain.admin.model.entity.AdminStrategyAwardEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyRuleEntity;

import java.util.List;

/**
 * 后台管理 - 策略仓储接口
 */
public interface IAdminStrategyRepository {

    void saveStrategy(AdminStrategyEntity entity);

    void updateStrategy(AdminStrategyEntity entity);

    void deleteStrategy(Long strategyId);

    AdminStrategyEntity queryStrategyById(Long strategyId);

    List<AdminStrategyEntity> queryStrategyPage(int offset, int limit, String strategyDesc);

    int countStrategy(String strategyDesc);

    void saveStrategyAward(AdminStrategyAwardEntity entity);

    void deleteStrategyAward(Long id);

    List<AdminStrategyAwardEntity> queryAwardListByStrategyId(Long strategyId);

    void saveStrategyRule(AdminStrategyRuleEntity entity);

    void deleteStrategyRule(Long id);

    List<AdminStrategyRuleEntity> queryRuleListByStrategyId(Long strategyId);

}
