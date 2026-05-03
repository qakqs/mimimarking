package cn.bugstack.infrastructure.persistent.repository.impl;

import cn.bugstack.domain.user.repository.ITokenRepository;
import cn.bugstack.infrastructure.persistent.redis.IRedisService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

/**
 * Redis-based token repository.
 */
@Repository
public class TokenRepository implements ITokenRepository {

    private static final String TOKEN_PREFIX = "login:token:";

    @Resource
    private IRedisService redisService;

    @Override
    public void saveToken(String token, String userId, long ttlMillis) {
        redisService.setValue(TOKEN_PREFIX + token, userId, ttlMillis);
    }

    @Override
    public String getUserId(String token) {
        return redisService.getValue(TOKEN_PREFIX + token);
    }

    @Override
    public void removeToken(String token) {
        redisService.remove(TOKEN_PREFIX + token);
    }

    @Override
    public boolean isTokenValid(String token) {
        return redisService.isExists(TOKEN_PREFIX + token);
    }

}
