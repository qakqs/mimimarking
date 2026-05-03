package cn.bugstack.domain.admin.service;

import cn.bugstack.domain.admin.model.aggregate.AdminCreditAdjustAggregate;
import cn.bugstack.domain.admin.model.entity.AdminCreditAccountEntity;
import cn.bugstack.domain.admin.model.entity.AdminCreditOrderEntity;

import java.util.List;

/**
 * 后台积分管理服务接口
 */
public interface IAdminCreditService {

    List<AdminCreditAccountEntity> accountList(int page, int pageSize, String userId);

    int accountCount(String userId);

    AdminCreditAccountEntity accountDetail(String userId);

    void adjust(AdminCreditAdjustAggregate aggregate);

    List<AdminCreditOrderEntity> orderList(int page, int pageSize, String userId);

    int orderCount(String userId);

}
