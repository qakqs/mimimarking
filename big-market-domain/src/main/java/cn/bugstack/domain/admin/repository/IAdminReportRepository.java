package cn.bugstack.domain.admin.repository;

import cn.bugstack.domain.admin.model.entity.AdminRaffleActivityOrderEntity;
import cn.bugstack.domain.admin.model.entity.AdminRebateOrderEntity;
import cn.bugstack.domain.admin.model.entity.AdminTaskEntity;
import cn.bugstack.domain.admin.model.entity.AdminUserAwardRecordEntity;
import cn.bugstack.domain.admin.model.valobj.AwardStatProjection;

import java.util.List;

/**
 * 后台管理 - 报表仓储接口
 */
public interface IAdminReportRepository {

    List<AdminRaffleActivityOrderEntity> queryOrderPage(int offset, int limit, String userId, Long activityId, String orderState);

    int countOrder(String userId, Long activityId, String orderState);

    AdminRaffleActivityOrderEntity queryOrderByOrderId(String orderId);

    List<AdminUserAwardRecordEntity> queryAwardRecordPage(int offset, int limit, String userId, Long activityId, String awardState);

    int countAwardRecord(String userId, Long activityId, String awardState);

    List<AwardStatProjection> queryAwardStatByActivityId(Long activityId);

    List<AdminRebateOrderEntity> queryRebateOrderPage(int offset, int limit, String userId, String rebateType);

    int countRebateOrder(String userId, String rebateType);

    List<AdminTaskEntity> queryTaskPage(int offset, int limit, String userId, String state, String topic);

    int countTask(String userId, String state, String topic);

}
