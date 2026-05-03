package cn.bugstack.domain.admin.service;

import cn.bugstack.domain.admin.model.entity.AdminRaffleActivityOrderEntity;
import cn.bugstack.domain.admin.model.entity.AdminRebateOrderEntity;
import cn.bugstack.domain.admin.model.entity.AdminTaskEntity;
import cn.bugstack.domain.admin.model.entity.AdminUserAwardRecordEntity;
import cn.bugstack.domain.admin.model.valobj.AwardStatProjection;

import java.util.List;

/**
 * 后台数据报表服务接口
 */
public interface IAdminReportService {

    List<AdminRaffleActivityOrderEntity> orderList(int page, int pageSize, String userId, Long activityId, String orderState);

    int orderCount(String userId, Long activityId, String orderState);

    AdminRaffleActivityOrderEntity orderDetail(String orderId);

    List<AdminUserAwardRecordEntity> awardRecordList(int page, int pageSize, Long activityId, String awardState);

    int awardRecordCount(Long activityId, String awardState);

    List<AwardStatProjection> awardStat(Long activityId);

    List<AdminRebateOrderEntity> rebateOrderList(int page, int pageSize, String rebateType);

    int rebateOrderCount(String rebateType);

    List<AdminTaskEntity> taskList(int page, int pageSize, String userId, String state, String topic);

    int taskCount(String userId, String state, String topic);

}
