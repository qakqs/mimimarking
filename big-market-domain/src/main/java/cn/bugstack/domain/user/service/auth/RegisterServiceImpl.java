package cn.bugstack.domain.user.service.auth;

import cn.bugstack.domain.user.model.aggregate.UserAggregate;
import cn.bugstack.domain.user.model.entity.UserEntity;
import cn.bugstack.domain.user.repository.IUserRepository;
import cn.bugstack.domain.user.service.IRegisterService;
import cn.bugstack.types.exception.AppException;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 注册服务实现
 */
@Service
public class RegisterServiceImpl implements IRegisterService {

    @Resource
    private IUserRepository userRepository;

    @Override
    public UserEntity register(String username, String password) {
        // 1. 参数校验
        if (StringUtils.length(username) < 3 || StringUtils.length(username) > 32) {
            throw new AppException("USER_REGISTER_USERNAME_INVALID", "用户名为 3~32 位");
        }
        if (StringUtils.length(password) < 6 || StringUtils.length(password) > 32) {
            throw new AppException("USER_REGISTER_PASSWORD_INVALID", "密码为 6~32 位");
        }

        // 2. 用户名唯一性校验
        UserEntity existing = userRepository.queryByUsername(username);
        if (existing != null) {
            throw new AppException("USER_REGISTER_DUP", "用户名已存在");
        }

        // 3. 创建用户聚合
        String encodedPassword = DigestUtils.sha256Hex(password);
        UserAggregate aggregate = UserAggregate.createForRegister(username, encodedPassword);

        // 4. 持久化
        userRepository.save(aggregate.getUserEntity());

        return aggregate.getUserEntity();
    }

    @Override
    public boolean isUsernameExist(String username) {
        UserEntity existing = userRepository.queryByUsername(username);
        return existing != null;
    }

}
