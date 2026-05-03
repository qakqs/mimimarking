package cn.bugstack.domain.admin.repository;

import cn.bugstack.domain.admin.model.entity.AdminCreditAccountEntity;
import cn.bugstack.domain.admin.model.entity.AdminCreditOrderEntity;

import java.util.List;

/**
 * 后台管理 - 积分仓储接口
 */
public interface IAdminCreditRepository {

    List<AdminCreditAccountEntity> queryCreditAccountPage(int offset, int limit, String userId);

    int countCreditAccount(String userId);

    AdminCreditAccountEntity queryCreditAccountByUserId(String userId);

    List<AdminCreditOrderEntity> queryCreditOrderPage(int offset, int limit, String userId);

    int countCreditOrder(String userId);

    void insertCreditOrder(AdminCreditOrderEntity entity);

    void updateCreditAccount(AdminCreditAccountEntity entity);

}
