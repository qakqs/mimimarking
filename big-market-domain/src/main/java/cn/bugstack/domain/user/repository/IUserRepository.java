package cn.bugstack.domain.user.repository;

import cn.bugstack.domain.user.model.entity.UserEntity;

/**
 * 用户仓储接口
 */
public interface IUserRepository {

    /** 保存用户 */
    void save(UserEntity userEntity);

    /** 根据用户名查询 */
    UserEntity queryByUsername(String username);

    /** 根据用户ID查询 */
    UserEntity queryByUserId(String userId);

    /** 更新用户 */
    void update(UserEntity userEntity);

}
