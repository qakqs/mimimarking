package cn.bugstack.domain.user.service;

import cn.bugstack.domain.user.model.entity.UserEntity;

/**
 * 注册服务接口
 */
public interface IRegisterService {

    /**
     * 注册用户
     * @param username 用户名
     * @param password 明文密码
     * @return 注册成功的用户实体
     */
    UserEntity register(String username, String password);

    /**
     * 检查用户名是否已存在
     */
    boolean isUsernameExist(String username);

}
