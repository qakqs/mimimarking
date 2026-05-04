package cn.bugstack.domain.admin.repository;

import cn.bugstack.domain.admin.model.entity.AdminUserEntity;

import java.util.List;

/**
 * 后台管理 - 用户仓储接口
 */
public interface IAdminUserRepository {

    List<AdminUserEntity> queryUserPage(int offset, int limit, String keyword, String status);

    int countUser(String keyword, String status);

    AdminUserEntity queryUserById(String userId);

    void updateUserStatus(String userId, Integer status);

}
