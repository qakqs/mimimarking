package cn.bugstack.infrastructure.persistent.repository.admin;

import cn.bugstack.domain.admin.model.entity.AdminDailyBehaviorRebateEntity;
import cn.bugstack.domain.admin.repository.IAdminRebateRepository;
import cn.bugstack.infrastructure.persistent.dao.IDailyBehaviorRebateDao;
import cn.bugstack.infrastructure.persistent.po.DailyBehaviorRebate;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台管理 - 返利配置仓储实现
 */
@Repository
public class AdminRebateRepository implements IAdminRebateRepository {

    @Resource
    private IDailyBehaviorRebateDao dailyBehaviorRebateDao;

    @Override
    public void saveRebateConfig(AdminDailyBehaviorRebateEntity entity) {
        dailyBehaviorRebateDao.insert(toDailyBehaviorRebatePO(entity));
    }

    @Override
    public void deleteRebateConfig(Long id) {
        dailyBehaviorRebateDao.deleteById(id);
    }

    @Override
    public void toggleRebateConfig(Long id, Integer state) {
        dailyBehaviorRebateDao.updateState(id, String.valueOf(state));
    }

    @Override
    public List<AdminDailyBehaviorRebateEntity> queryRebateConfigPage(int offset, int limit, String behaviorType, String state) {
        List<DailyBehaviorRebate> list = dailyBehaviorRebateDao.queryPage(offset, limit, behaviorType, state);
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public int countRebateConfig(String behaviorType, String state) {
        return dailyBehaviorRebateDao.count(behaviorType, state);
    }

    private DailyBehaviorRebate toDailyBehaviorRebatePO(AdminDailyBehaviorRebateEntity e) {
        DailyBehaviorRebate po = new DailyBehaviorRebate();
        po.setId(e.getId()); po.setBehaviorType(e.getBehaviorType());
        po.setRebateDesc(e.getRebateDesc()); po.setRebateType(e.getRebateType());
        po.setRebateConfig(e.getRebateConfig()); po.setState(e.getState());
        po.setCreateTime(e.getCreateTime()); po.setUpdateTime(e.getUpdateTime());
        return po;
    }

    private AdminDailyBehaviorRebateEntity toEntity(DailyBehaviorRebate po) {
        if (po == null) return null;
        return AdminDailyBehaviorRebateEntity.builder()
                .id(po.getId()).behaviorType(po.getBehaviorType())
                .rebateDesc(po.getRebateDesc()).rebateType(po.getRebateType())
                .rebateConfig(po.getRebateConfig()).state(po.getState())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

}
