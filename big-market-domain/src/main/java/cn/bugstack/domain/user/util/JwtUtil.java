package cn.bugstack.domain.user.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * JWT utility for token creation, verification and parsing.
 */
public final class JwtUtil {

    private JwtUtil() {}

    public static String create(String userId, String username, String secret, long ttlMillis) {
        Date now = new Date();
        return JWT.create()
                .withSubject(userId)
                .withClaim("username", username)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + ttlMillis))
                .sign(Algorithm.HMAC256(secret));
    }

    public static DecodedJWT verify(String token, String secret) {
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }

    public static String getUserId(String token, String secret) {
        return verify(token, secret).getSubject();
    }

    public static String getUsername(String token, String secret) {
        return verify(token, secret).getClaim("username").asString();
    }

}
