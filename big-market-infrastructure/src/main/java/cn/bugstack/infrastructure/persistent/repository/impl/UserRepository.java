package cn.bugstack.infrastructure.persistent.repository.impl;

import cn.bugstack.domain.user.model.entity.UserEntity;
import cn.bugstack.domain.user.repository.IUserRepository;
import cn.bugstack.infrastructure.persistent.dao.IUserDao;
import cn.bugstack.infrastructure.persistent.po.User;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

/**
 * 用户仓储实现
 */
@Repository
public class UserRepository implements IUserRepository {

    @Resource
    private IUserDao userDao;

    @Override
    public void save(UserEntity userEntity) {
        User po = toPO(userEntity);
        userDao.insert(po);
    }

    @Override
    public UserEntity queryByUsername(String username) {
        User po = userDao.queryByUsername(username);
        return toEntity(po);
    }

    @Override
    public UserEntity queryByUserId(String userId) {
        User po = userDao.queryByUserId(userId);
        return toEntity(po);
    }

    @Override
    public void update(UserEntity userEntity) {
        userDao.update(toPO(userEntity));
    }

    // ===== 转换 =====

    private User toPO(UserEntity entity) {
        if (entity == null) return null;
        User po = new User();
        po.setUserId(entity.getUserId());
        po.setUsername(entity.getUsername());
        po.setPassword(entity.getPassword());
        po.setName(entity.getName());
        po.setEmail(entity.getEmail());
        po.setPhone(entity.getPhone());
        po.setStatus(entity.getStatus());
        po.setCreateTime(entity.getCreateTime());
        po.setUpdateTime(entity.getUpdateTime());
        return po;
    }

    private UserEntity toEntity(User po) {
        if (po == null) return null;
        return UserEntity.builder()
                .userId(po.getUserId())
                .username(po.getUsername())
                .password(po.getPassword())
                .name(po.getName())
                .email(po.getEmail())
                .phone(po.getPhone())
                .status(po.getStatus())
                .createTime(po.getCreateTime())
                .updateTime(po.getUpdateTime())
                .build();
    }

}
