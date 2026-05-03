package cn.bugstack.infrastructure.persistent.repository.impl;

import cn.bugstack.domain.user.repository.ILoginAttemptRepository;
import cn.bugstack.infrastructure.persistent.redis.IRedisService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

/**
 * Redis-based login attempt repository.
 */
@Repository
public class LoginAttemptRepository implements ILoginAttemptRepository {

    private static final String ATTEMPT_PREFIX = "login:attempt:";
    private static final String LOCK_SUFFIX = ":lock";

    @Resource
    private IRedisService redisService;

    @Override
    public long incrAttempt(String key) {
        return redisService.incr(ATTEMPT_PREFIX + key);
    }

    @Override
    public void setLock(String key, long ttlMillis) {
        redisService.setValue(ATTEMPT_PREFIX + key + LOCK_SUFFIX, "1", ttlMillis);
    }

    @Override
    public boolean isLocked(String key) {
        return redisService.isExists(ATTEMPT_PREFIX + key + LOCK_SUFFIX);
    }

    @Override
    public void clearAttempt(String key) {
        redisService.remove(ATTEMPT_PREFIX + key);
        redisService.remove(ATTEMPT_PREFIX + key + LOCK_SUFFIX);
    }

}
