package cn.bugstack.domain.user.service.auth;

import cn.bugstack.domain.user.model.entity.UserEntity;
import cn.bugstack.domain.user.repository.ILoginAttemptRepository;
import cn.bugstack.domain.user.repository.ITokenRepository;
import cn.bugstack.domain.user.repository.IUserRepository;
import cn.bugstack.domain.user.service.ILoginService;
import cn.bugstack.domain.user.util.JwtUtil;
import cn.bugstack.domain.user.util.PasswordUtil;
import cn.bugstack.types.exception.AppException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 登录服务实现
 */
@Service
public class LoginServiceImpl implements ILoginService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_MINUTES = 15;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.ttl}")
    private long jwtTtl;

    @Resource
    private IUserRepository userRepository;

    @Resource
    private ITokenRepository tokenRepository;

    @Resource
    private ILoginAttemptRepository loginAttemptRepository;

    @Override
    public String login(String username, String password) {
        // 1. 参数校验
        if (StringUtils.isAnyBlank(username, password)) {
            throw new AppException("USER_LOGIN_PARAM_BLANK", "用户名或密码为空");
        }

        // 2. 检查是否已锁定
        if (loginAttemptRepository.isLocked(username)) {
            throw new AppException("USER_LOGIN_LOCKED", "账户已锁定，请" + LOCK_MINUTES + "分钟后重试");
        }

        // 3. 查询用户
        UserEntity user = userRepository.queryByUsername(username);
        if (user == null) {
            recordFailedAttempt(username);
            throw new AppException("USER_LOGIN_NOT_FOUND", "用户不存在");
        }

        // 4. 密码校验
        if (!PasswordUtil.matches(password, user.getPassword())) {
            recordFailedAttempt(username);
            throw new AppException("USER_LOGIN_PASSWORD_ERROR", "密码错误");
        }

        // 5. 清除失败记录
        loginAttemptRepository.clearAttempt(username);

        // 6. 生成 JWT 并存入 Redis
        String token = JwtUtil.create(user.getUserId(), user.getUsername(), jwtSecret, jwtTtl);
        tokenRepository.saveToken(token, user.getUserId(), jwtTtl);

        return token;
    }

    @Override
    public boolean checkToken(String token) {
        if (StringUtils.isBlank(token)) return false;
        try {
            JwtUtil.verify(token, jwtSecret);
            return tokenRepository.isTokenValid(token);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String openid(String token) {
        return tokenRepository.getUserId(token);
    }

    @Override
    public UserEntity queryUserByToken(String token) {
        String userId = tokenRepository.getUserId(token);
        if (userId == null) {
            return null;
        }
        return userRepository.queryByUserId(userId);
    }

    @Override
    public void logout(String token) {
        tokenRepository.removeToken(token);
    }

    private void recordFailedAttempt(String username) {
        long attempts = loginAttemptRepository.incrAttempt(username);
        if (attempts >= MAX_ATTEMPTS) {
            loginAttemptRepository.setLock(username, LOCK_MINUTES * 60 * 1000L);
        }
    }

}
