package cn.bugstack.infrastructure.persistent.repository.admin;

import cn.bugstack.domain.admin.model.entity.AdminStrategyAwardEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyRuleEntity;
import cn.bugstack.domain.admin.repository.IAdminStrategyRepository;
import cn.bugstack.infrastructure.persistent.dao.IStrategyAwardDao;
import cn.bugstack.infrastructure.persistent.dao.IStrategyDao;
import cn.bugstack.infrastructure.persistent.dao.IStrategyRuleDao;
import cn.bugstack.infrastructure.persistent.po.Strategy;
import cn.bugstack.infrastructure.persistent.po.StrategyAward;
import cn.bugstack.infrastructure.persistent.po.StrategyRule;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台管理 - 策略仓储实现
 */
@Repository
public class AdminStrategyRepository implements IAdminStrategyRepository {

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Override
    public void saveStrategy(AdminStrategyEntity entity) {
        strategyDao.insert(toStrategyPO(entity));
    }

    @Override
    public void updateStrategy(AdminStrategyEntity entity) {
        strategyDao.update(toStrategyPO(entity));
    }

    @Override
    public void deleteStrategy(Long strategyId) {
        strategyDao.deleteByStrategyId(strategyId);
    }

    @Override
    public AdminStrategyEntity queryStrategyById(Long strategyId) {
        Strategy po = strategyDao.queryStrategyByStrategyId(strategyId);
        return toStrategyEntity(po);
    }

    @Override
    public List<AdminStrategyEntity> queryStrategyPage(int offset, int limit, String strategyDesc) {
        List<Strategy> list = strategyDao.queryStrategyPage(offset, limit, strategyDesc);
        return list.stream().map(this::toStrategyEntity).collect(Collectors.toList());
    }

    @Override
    public int countStrategy(String strategyDesc) {
        return strategyDao.countStrategy(strategyDesc);
    }

    @Override
    public void saveStrategyAward(AdminStrategyAwardEntity entity) {
        strategyAwardDao.insert(toStrategyAwardPO(entity));
    }

    @Override
    public void deleteStrategyAward(Long id) {
        strategyAwardDao.deleteById(id);
    }

    @Override
    public List<AdminStrategyAwardEntity> queryAwardListByStrategyId(Long strategyId) {
        List<StrategyAward> list = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        return list.stream().map(this::toStrategyAwardEntity).collect(Collectors.toList());
    }

    @Override
    public void saveStrategyRule(AdminStrategyRuleEntity entity) {
        strategyRuleDao.insert(toStrategyRulePO(entity));
    }

    @Override
    public void deleteStrategyRule(Long id) {
        strategyRuleDao.deleteById(id);
    }

    @Override
    public List<AdminStrategyRuleEntity> queryRuleListByStrategyId(Long strategyId) {
        List<StrategyRule> list = strategyRuleDao.queryByStrategyId(strategyId);
        return list.stream().map(this::toStrategyRuleEntity).collect(Collectors.toList());
    }

    // ===== PO <-> Entity mapping =====

    private Strategy toStrategyPO(AdminStrategyEntity e) {
        Strategy po = new Strategy();
        po.setId(e.getId());
        po.setStrategyId(e.getStrategyId());
        po.setStrategyDesc(e.getStrategyDesc());
        po.setRuleModels(e.getRuleModels());
        po.setCreateTime(e.getCreateTime());
        po.setUpdateTime(e.getUpdateTime());
        return po;
    }

    private AdminStrategyEntity toStrategyEntity(Strategy po) {
        if (po == null) return null;
        return AdminStrategyEntity.builder()
                .id(po.getId()).strategyId(po.getStrategyId())
                .strategyDesc(po.getStrategyDesc()).ruleModels(po.getRuleModels())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

    private StrategyAward toStrategyAwardPO(AdminStrategyAwardEntity e) {
        StrategyAward po = new StrategyAward();
        po.setId(e.getId()); po.setStrategyId(e.getStrategyId());
        po.setAwardId(e.getAwardId()); po.setAwardTitle(e.getAwardTitle());
        po.setAwardSubtitle(e.getAwardSubtitle()); po.setAwardCount(e.getAwardCount());
        po.setAwardCountSurplus(e.getAwardCountSurplus()); po.setAwardRate(e.getAwardRate());
        po.setRuleModels(e.getRuleModels()); po.setSort(e.getSort());
        po.setCreateTime(e.getCreateTime()); po.setUpdateTime(e.getUpdateTime());
        return po;
    }

    private AdminStrategyAwardEntity toStrategyAwardEntity(StrategyAward po) {
        if (po == null) return null;
        return AdminStrategyAwardEntity.builder()
                .id(po.getId()).strategyId(po.getStrategyId()).awardId(po.getAwardId())
                .awardTitle(po.getAwardTitle()).awardSubtitle(po.getAwardSubtitle())
                .awardCount(po.getAwardCount()).awardCountSurplus(po.getAwardCountSurplus())
                .awardRate(po.getAwardRate()).ruleModels(po.getRuleModels()).sort(po.getSort())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

    private StrategyRule toStrategyRulePO(AdminStrategyRuleEntity e) {
        StrategyRule po = new StrategyRule();
        po.setId(e.getId()); po.setStrategyId(e.getStrategyId());
        po.setAwardId(e.getAwardId()); po.setRuleType(e.getRuleType());
        po.setRuleModel(e.getRuleModel()); po.setRuleValue(e.getRuleValue());
        po.setRuleDesc(e.getRuleDesc());
        po.setCreateTime(e.getCreateTime()); po.setUpdateTime(e.getUpdateTime());
        return po;
    }

    private AdminStrategyRuleEntity toStrategyRuleEntity(StrategyRule po) {
        if (po == null) return null;
        return AdminStrategyRuleEntity.builder()
                .id(po.getId()).strategyId(po.getStrategyId()).awardId(po.getAwardId())
                .ruleType(po.getRuleType()).ruleModel(po.getRuleModel())
                .ruleValue(po.getRuleValue()).ruleDesc(po.getRuleDesc())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

}
