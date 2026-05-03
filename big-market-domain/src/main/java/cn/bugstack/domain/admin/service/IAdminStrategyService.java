package cn.bugstack.domain.admin.service;

import cn.bugstack.domain.admin.model.entity.AdminStrategyAwardEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyRuleEntity;

import java.util.List;

/**
 * 后台策略管理服务接口
 */
public interface IAdminStrategyService {

    void create(AdminStrategyEntity entity);

    void update(AdminStrategyEntity entity);

    void delete(Long strategyId);

    AdminStrategyEntity detail(Long strategyId);

    List<AdminStrategyEntity> list(int page, int pageSize, String strategyDesc);

    int count(String strategyDesc);

    List<AdminStrategyAwardEntity> awardList(Long strategyId);

    void saveAward(AdminStrategyAwardEntity entity);

    void deleteAward(Long id);

    List<AdminStrategyRuleEntity> ruleList(Long strategyId);

    void saveRule(AdminStrategyRuleEntity entity);

    void deleteRule(Long id);

}
