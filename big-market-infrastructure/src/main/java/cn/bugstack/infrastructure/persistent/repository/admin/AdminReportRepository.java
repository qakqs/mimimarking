package cn.bugstack.infrastructure.persistent.repository.admin;

import cn.bugstack.domain.admin.model.entity.AdminRaffleActivityOrderEntity;
import cn.bugstack.domain.admin.model.entity.AdminRebateOrderEntity;
import cn.bugstack.domain.admin.model.entity.AdminTaskEntity;
import cn.bugstack.domain.admin.model.entity.AdminUserAwardRecordEntity;
import cn.bugstack.domain.admin.model.valobj.AwardStatProjection;
import cn.bugstack.domain.admin.repository.IAdminReportRepository;
import cn.bugstack.infrastructure.persistent.dao.IRaffleActivityOrderDao;
import cn.bugstack.infrastructure.persistent.dao.ITaskDao;
import cn.bugstack.infrastructure.persistent.dao.IUserAwardRecordDao;
import cn.bugstack.infrastructure.persistent.dao.IUserBehaviorRebateOrderDao;
import cn.bugstack.infrastructure.persistent.po.RaffleActivityOrder;
import cn.bugstack.infrastructure.persistent.po.Task;
import cn.bugstack.infrastructure.persistent.po.UserAwardRecord;
import cn.bugstack.infrastructure.persistent.po.UserBehaviorRebateOrder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后台管理 - 报表仓储实现
 */
@Repository
public class AdminReportRepository implements IAdminReportRepository {

    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;

    @Resource
    private IUserAwardRecordDao userAwardRecordDao;

    @Resource
    private IUserBehaviorRebateOrderDao userBehaviorRebateOrderDao;

    @Resource
    private ITaskDao taskDao;

    @Override
    public List<AdminRaffleActivityOrderEntity> queryOrderPage(int offset, int limit, String userId, Long activityId, String orderState) {
        List<RaffleActivityOrder> list = raffleActivityOrderDao.queryPage(offset, limit, userId, activityId, orderState);
        return list.stream().map(this::toOrderEntity).collect(Collectors.toList());
    }

    @Override
    public int countOrder(String userId, Long activityId, String orderState) {
        return raffleActivityOrderDao.count(userId, activityId, orderState);
    }

    @Override
    public AdminRaffleActivityOrderEntity queryOrderByOrderId(String orderId) {
        RaffleActivityOrder req = new RaffleActivityOrder();
        req.setOrderId(orderId);
        RaffleActivityOrder po = raffleActivityOrderDao.queryRaffleActivityOrder(req);
        return toOrderEntity(po);
    }

    @Override
    public List<AdminUserAwardRecordEntity> queryAwardRecordPage(int offset, int limit, String userId, Long activityId, String awardState) {
        List<UserAwardRecord> list = userAwardRecordDao.queryPage(offset, limit, userId, activityId, awardState);
        return list.stream().map(this::toAwardRecordEntity).collect(Collectors.toList());
    }

    @Override
    public int countAwardRecord(String userId, Long activityId, String awardState) {
        return userAwardRecordDao.count(userId, activityId, awardState);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AwardStatProjection> queryAwardStatByActivityId(Long activityId) {
        List<Map<String, Object>> rows = userAwardRecordDao.queryAwardStat(activityId);
        return rows.stream().map(row -> AwardStatProjection.builder()
                .awardId((Integer) row.get("awardId"))
                .awardTitle((String) row.get("awardTitle"))
                .awardCount((Long) row.get("awardCount"))
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<AdminRebateOrderEntity> queryRebateOrderPage(int offset, int limit, String userId, String rebateType) {
        List<UserBehaviorRebateOrder> list = userBehaviorRebateOrderDao.queryPage(offset, limit, userId, rebateType);
        return list.stream().map(this::toRebateOrderEntity).collect(Collectors.toList());
    }

    @Override
    public int countRebateOrder(String userId, String rebateType) {
        return userBehaviorRebateOrderDao.count(userId, rebateType);
    }

    @Override
    public List<AdminTaskEntity> queryTaskPage(int offset, int limit, String userId, String state, String topic) {
        List<Task> list = taskDao.queryPage(offset, limit, userId, state, topic);
        return list.stream().map(this::toTaskEntity).collect(Collectors.toList());
    }

    @Override
    public int countTask(String userId, String state, String topic) {
        return taskDao.count(userId, state, topic);
    }

    // ===== PO <-> Entity mapping =====

    private AdminRaffleActivityOrderEntity toOrderEntity(RaffleActivityOrder po) {
        if (po == null) return null;
        return AdminRaffleActivityOrderEntity.builder()
                .id(po.getId()).userId(po.getUserId()).activityId(po.getActivityId())
                .sku(po.getSku()).activityName(po.getActivityName()).strategyId(po.getStrategyId())
                .orderId(po.getOrderId()).orderTime(po.getOrderTime())
                .totalCount(po.getTotalCount()).dayCount(po.getDayCount()).monthCount(po.getMonthCount())
                .state(po.getState()).outBusinessNo(po.getOutBusinessNo()).payAmount(po.getPayAmount())
                .endDateTime(po.getEndDateTime())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

    private AdminUserAwardRecordEntity toAwardRecordEntity(UserAwardRecord po) {
        if (po == null) return null;
        return AdminUserAwardRecordEntity.builder()
                .id(po.getId()).userId(po.getUserId()).activityId(po.getActivityId())
                .strategyId(po.getStrategyId()).orderId(po.getOrderId()).awardId(po.getAwardId())
                .awardTitle(po.getAwardTitle()).awardTime(po.getAwardTime())
                .awardState(po.getAwardState())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

    private AdminRebateOrderEntity toRebateOrderEntity(UserBehaviorRebateOrder po) {
        if (po == null) return null;
        return AdminRebateOrderEntity.builder()
                .id(po.getId()).userId(po.getUserId()).orderId(po.getOrderId())
                .outBusinessNo(po.getOutBusinessNo()).behaviorType(po.getBehaviorType())
                .rebateDesc(po.getRebateDesc()).rebateType(po.getRebateType())
                .rebateConfig(po.getRebateConfig()).bizId(po.getBizId())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

    private AdminTaskEntity toTaskEntity(Task po) {
        if (po == null) return null;
        return AdminTaskEntity.builder()
                .id(po.getId()).userId(po.getUserId()).messageId(po.getMessageId())
                .topic(po.getTopic()).message(po.getMessage()).state(po.getState())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

}
