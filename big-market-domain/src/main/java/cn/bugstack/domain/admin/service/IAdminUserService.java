package cn.bugstack.domain.admin.service;

import cn.bugstack.domain.admin.model.entity.AdminUserEntity;

import java.util.List;

/**
 * 后台用户管理服务接口
 */
public interface IAdminUserService {

    List<AdminUserEntity> list(int page, int pageSize, String keyword, String status);

    int count(String keyword, String status);

    AdminUserEntity detail(String userId);

    void disable(String userId, Integer status);

}
