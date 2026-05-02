package cn.bugstack.domain.user.service.auth;

import cn.bugstack.domain.user.model.entity.UserEntity;
import cn.bugstack.domain.user.repository.IUserRepository;
import cn.bugstack.domain.user.service.ILoginService;
import cn.bugstack.types.exception.AppException;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录服务实现
 */
@Service
public class LoginServiceImpl implements ILoginService {

    @Resource
    private IUserRepository userRepository;

    /** 简易 Token 存储；生产环境应替换为 Redis + JWT */
    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();

    @Override
    public String login(String username, String password) {
        // 1. 参数校验
        if (StringUtils.isAnyBlank(username, password)) {
            throw new AppException("USER_LOGIN_PARAM_BLANK", "用户名或密码为空");
        }

        // 2. 查询用户
        UserEntity user = userRepository.queryByUsername(username);
        if (user == null) {
            throw new AppException("USER_LOGIN_NOT_FOUND", "用户不存在");
        }

        // 3. 密码校验
        String encoded = DigestUtils.sha256Hex(password);
        if (!encoded.equals(user.getPassword())) {
            throw new AppException("USER_LOGIN_PASSWORD_ERROR", "密码错误");
        }

        // 4. 生成 Token
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenStore.put(token, user.getUserId());

        return token;
    }

    @Override
    public boolean checkToken(String token) {
        if (StringUtils.isBlank(token)) return false;
        return tokenStore.containsKey(token);
    }

    @Override
    public String openid(String token) {
        return tokenStore.get(token);
    }

    @Override
    public UserEntity queryUserByToken(String token) {
        String userId = tokenStore.get(token);
        if (userId == null) return null;
        return userRepository.queryByUserId(userId);
    }

}
