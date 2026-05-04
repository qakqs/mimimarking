package cn.bugstack.domain.admin.service.impl;

import cn.bugstack.domain.admin.model.entity.AdminRaffleActivityOrderEntity;
import cn.bugstack.domain.admin.model.entity.AdminRebateOrderEntity;
import cn.bugstack.domain.admin.model.entity.AdminTaskEntity;
import cn.bugstack.domain.admin.model.entity.AdminUserAwardRecordEntity;
import cn.bugstack.domain.admin.model.valobj.AwardStatProjection;
import cn.bugstack.domain.admin.repository.IAdminReportRepository;
import cn.bugstack.domain.admin.service.IAdminReportService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminReportServiceImpl implements IAdminReportService {

    private final IAdminReportRepository adminReportRepository;

    public AdminReportServiceImpl(IAdminReportRepository adminReportRepository) {
        this.adminReportRepository = adminReportRepository;
    }

    @Override
    public List<AdminRaffleActivityOrderEntity> orderList(int page, int pageSize, String userId, Long activityId, String orderState) {
        int offset = (page - 1) * pageSize;
        return adminReportRepository.queryOrderPage(offset, pageSize, userId, activityId, orderState);
    }

    @Override
    public int orderCount(String userId, Long activityId, String orderState) {
        return adminReportRepository.countOrder(userId, activityId, orderState);
    }

    @Override
    public AdminRaffleActivityOrderEntity orderDetail(String orderId) {
        return adminReportRepository.queryOrderByOrderId(orderId);
    }

    @Override
    public List<AdminUserAwardRecordEntity> awardRecordList(int page, int pageSize, String userId, Long activityId, String awardState) {
        int offset = (page - 1) * pageSize;
        return adminReportRepository.queryAwardRecordPage(offset, pageSize, userId, activityId, awardState);
    }

    @Override
    public int awardRecordCount(String userId, Long activityId, String awardState) {
        return adminReportRepository.countAwardRecord(userId, activityId, awardState);
    }

    @Override
    public List<AwardStatProjection> awardStat(Long activityId) {
        return adminReportRepository.queryAwardStatByActivityId(activityId);
    }

    @Override
    public List<AdminRebateOrderEntity> rebateOrderList(int page, int pageSize, String userId, String rebateType) {
        int offset = (page - 1) * pageSize;
        return adminReportRepository.queryRebateOrderPage(offset, pageSize, userId, rebateType);
    }

    @Override
    public int rebateOrderCount(String userId, String rebateType) {
        return adminReportRepository.countRebateOrder(userId, rebateType);
    }

    @Override
    public List<AdminTaskEntity> taskList(int page, int pageSize, String userId, String state, String topic) {
        int offset = (page - 1) * pageSize;
        return adminReportRepository.queryTaskPage(offset, pageSize, userId, state, topic);
    }

    @Override
    public int taskCount(String userId, String state, String topic) {
        return adminReportRepository.countTask(userId, state, topic);
    }

}
