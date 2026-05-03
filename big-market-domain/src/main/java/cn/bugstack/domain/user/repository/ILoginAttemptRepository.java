package cn.bugstack.domain.user.repository;

/**
 * Login attempt tracking repository.
 */
public interface ILoginAttemptRepository {

    /**
     * Atomically increment and return the attempt count.
     */
    long incrAttempt(String key);

    /**
     * Set a lock flag with TTL.
     */
    void setLock(String key, long ttlMillis);

    /**
     * Check whether the lock flag exists.
     */
    boolean isLocked(String key);

    /**
     * Remove attempt count and lock flag.
     */
    void clearAttempt(String key);

}
