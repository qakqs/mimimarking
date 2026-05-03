package cn.bugstack.infrastructure.persistent.repository.admin;

import cn.bugstack.domain.admin.model.entity.AdminActivityCountEntity;
import cn.bugstack.domain.admin.model.entity.AdminActivityEntity;
import cn.bugstack.domain.admin.model.entity.AdminActivitySkuEntity;
import cn.bugstack.domain.admin.repository.IAdminActivityRepository;
import cn.bugstack.infrastructure.persistent.dao.IRaffleActivityCountDao;
import cn.bugstack.infrastructure.persistent.dao.IRaffleActivityDao;
import cn.bugstack.infrastructure.persistent.dao.IRaffleActivitySkuDao;
import cn.bugstack.infrastructure.persistent.po.RaffleActivity;
import cn.bugstack.infrastructure.persistent.po.RaffleActivityCount;
import cn.bugstack.infrastructure.persistent.po.RaffleActivitySku;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台管理 - 活动仓储实现
 */
@Repository
public class AdminActivityRepository implements IAdminActivityRepository {

    @Resource
    private IRaffleActivityDao raffleActivityDao;

    @Resource
    private IRaffleActivitySkuDao raffleActivitySkuDao;

    @Resource
    private IRaffleActivityCountDao raffleActivityCountDao;

    @Override
    public void saveActivity(AdminActivityEntity entity) {
        raffleActivityDao.insert(toActivityPO(entity));
    }

    @Override
    public void updateActivity(AdminActivityEntity entity) {
        raffleActivityDao.update(toActivityPO(entity));
    }

    @Override
    public void deleteActivity(Long activityId) {
        raffleActivityDao.deleteByActivityId(activityId);
    }

    @Override
    public AdminActivityEntity queryActivityById(Long activityId) {
        RaffleActivity po = raffleActivityDao.queryRaffleActivityByActivityId(activityId);
        return toActivityEntity(po);
    }

    @Override
    public List<AdminActivityEntity> queryActivityPage(int offset, int limit, String activityName, String state) {
        List<RaffleActivity> list = raffleActivityDao.queryActivityPage(offset, limit, activityName, state);
        return list.stream().map(this::toActivityEntity).collect(Collectors.toList());
    }

    @Override
    public int countActivity(String activityName, String state) {
        return raffleActivityDao.countActivity(activityName, state);
    }

    @Override
    public void toggleActivityStatus(Long activityId, Integer state) {
        raffleActivityDao.updateActivityStatus(activityId, state);
    }

    @Override
    public void saveActivitySku(AdminActivitySkuEntity entity) {
        raffleActivitySkuDao.insert(toActivitySkuPO(entity));
    }

    @Override
    public void deleteActivitySku(Long sku) {
        raffleActivitySkuDao.deleteBySku(sku);
    }

    @Override
    public List<AdminActivitySkuEntity> querySkuListByActivityId(Long activityId) {
        List<RaffleActivitySku> list = raffleActivitySkuDao.queryActivitySkuListByActivityId(activityId);
        return list.stream().map(this::toActivitySkuEntity).collect(Collectors.toList());
    }

    @Override
    public void adjustSkuStock(Long sku, Integer delta) {
        raffleActivitySkuDao.adjustSkuStock(sku, delta);
    }

    @Override
    public void saveActivityCount(AdminActivityCountEntity entity) {
        raffleActivityCountDao.insert(toActivityCountPO(entity));
    }

    @Override
    public AdminActivityCountEntity queryActivityCountByActivityId(Long activityId) {
        RaffleActivityCount po = raffleActivityCountDao.queryByActivityId(activityId);
        return toActivityCountEntity(po);
    }

    // ===== PO <-> Entity mapping =====

    private RaffleActivity toActivityPO(AdminActivityEntity e) {
        RaffleActivity po = new RaffleActivity();
        po.setId(e.getId());
        po.setActivityId(e.getActivityId());
        po.setActivityName(e.getActivityName());
        po.setActivityDesc(e.getActivityDesc());
        po.setBeginDateTime(e.getBeginDateTime());
        po.setEndDateTime(e.getEndDateTime());
        po.setStrategyId(e.getStrategyId());
        po.setState(e.getState());
        po.setCreateTime(e.getCreateTime());
        po.setUpdateTime(e.getUpdateTime());
        return po;
    }

    private AdminActivityEntity toActivityEntity(RaffleActivity po) {
        if (po == null) return null;
        return AdminActivityEntity.builder()
                .id(po.getId())
                .activityId(po.getActivityId())
                .activityName(po.getActivityName())
                .activityDesc(po.getActivityDesc())
                .beginDateTime(po.getBeginDateTime())
                .endDateTime(po.getEndDateTime())
                .strategyId(po.getStrategyId())
                .state(po.getState())
                .createTime(po.getCreateTime())
                .updateTime(po.getUpdateTime())
                .build();
    }

    private RaffleActivitySku toActivitySkuPO(AdminActivitySkuEntity e) {
        RaffleActivitySku po = new RaffleActivitySku();
        po.setSku(e.getSku());
        po.setActivityId(e.getActivityId());
        po.setActivityCountId(e.getActivityCountId());
        po.setStockCount(e.getStockCount());
        po.setStockCountSurplus(e.getStockCountSurplus());
        po.setProductAmount(e.getProductAmount());
        po.setCreateTime(e.getCreateTime());
        po.setUpdateTime(e.getUpdateTime());
        return po;
    }

    private AdminActivitySkuEntity toActivitySkuEntity(RaffleActivitySku po) {
        if (po == null) return null;
        return AdminActivitySkuEntity.builder()
                .sku(po.getSku())
                .activityId(po.getActivityId())
                .activityCountId(po.getActivityCountId())
                .stockCount(po.getStockCount())
                .stockCountSurplus(po.getStockCountSurplus())
                .productAmount(po.getProductAmount())
                .createTime(po.getCreateTime())
                .updateTime(po.getUpdateTime())
                .build();
    }

    private RaffleActivityCount toActivityCountPO(AdminActivityCountEntity e) {
        RaffleActivityCount po = new RaffleActivityCount();
        po.setId(e.getId());
        po.setActivityCountId(e.getActivityCountId());
        po.setTotalCount(e.getTotalCount());
        po.setDayCount(e.getDayCount());
        po.setMonthCount(e.getMonthCount());
        po.setCreateTime(e.getCreateTime());
        po.setUpdateTime(e.getUpdateTime());
        return po;
    }

    private AdminActivityCountEntity toActivityCountEntity(RaffleActivityCount po) {
        if (po == null) return null;
        return AdminActivityCountEntity.builder()
                .id(po.getId())
                .activityCountId(po.getActivityCountId())
                .totalCount(po.getTotalCount())
                .dayCount(po.getDayCount())
                .monthCount(po.getMonthCount())
                .createTime(po.getCreateTime())
                .updateTime(po.getUpdateTime())
                .build();
    }

}
