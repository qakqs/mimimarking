package cn.bugstack.domain.user.repository;

/**
 * Token persistence repository.
 */
public interface ITokenRepository {

    void saveToken(String token, String userId, long ttlMillis);

    String getUserId(String token);

    void removeToken(String token);

    boolean isTokenValid(String token);

}
