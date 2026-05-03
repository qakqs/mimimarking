package cn.bugstack.domain.admin.service.impl;

import cn.bugstack.domain.admin.model.entity.AdminUserEntity;
import cn.bugstack.domain.admin.repository.IAdminUserRepository;
import cn.bugstack.domain.admin.service.IAdminUserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminUserServiceImpl implements IAdminUserService {

    private final IAdminUserRepository adminUserRepository;

    public AdminUserServiceImpl(IAdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public List<AdminUserEntity> list(int page, int pageSize, String keyword) {
        int offset = (page - 1) * pageSize;
        return adminUserRepository.queryUserPage(offset, pageSize, keyword);
    }

    @Override
    public int count(String keyword) {
        return adminUserRepository.countUser(keyword);
    }

    @Override
    public AdminUserEntity detail(String userId) {
        return adminUserRepository.queryUserById(userId);
    }

    @Override
    public void disable(String userId, Integer status) {
        adminUserRepository.updateUserStatus(userId, status);
    }

}
