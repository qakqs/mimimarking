package cn.bugstack.infrastructure.persistent.repository.admin;

import cn.bugstack.domain.admin.model.entity.AdminUserEntity;
import cn.bugstack.domain.admin.repository.IAdminUserRepository;
import cn.bugstack.infrastructure.persistent.dao.IUserDao;
import cn.bugstack.infrastructure.persistent.po.User;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台管理 - 用户仓储实现
 */
@Repository
public class AdminUserRepository implements IAdminUserRepository {

    @Resource
    private IUserDao userDao;

    @Override
    public List<AdminUserEntity> queryUserPage(int offset, int limit, String keyword) {
        List<User> list = userDao.queryPage(offset, limit, keyword);
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public int countUser(String keyword) {
        return userDao.count(keyword);
    }

    @Override
    public AdminUserEntity queryUserById(String userId) {
        User po = userDao.queryByUserId(userId);
        return toEntity(po);
    }

    @Override
    public void updateUserStatus(String userId, Integer status) {
        userDao.updateStatus(userId, status);
    }

    private AdminUserEntity toEntity(User po) {
        if (po == null) return null;
        return AdminUserEntity.builder()
                .id(po.getId()).userId(po.getUserId()).username(po.getUsername())
                .password(po.getPassword()).name(po.getName()).email(po.getEmail())
                .phone(po.getPhone()).status(po.getStatus())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime()).build();
    }

}
