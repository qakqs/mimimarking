package cn.bugstack.domain.user.service;

import cn.bugstack.domain.user.model.entity.UserEntity;

/**
 * 登录服务接口
 */
public interface ILoginService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 明文密码
     * @return 登录 Token
     */
    String login(String username, String password);

    /**
     * 校验 Token
     */
    boolean checkToken(String token);

    /**
     * 从 Token 中解析用户ID
     */
    String openid(String token);

    /**
     * 通过 Token 获取用户实体
     */
    UserEntity queryUserByToken(String token);

    /**
     * 登出，使 Token 失效
     */
    void logout(String token);

}
