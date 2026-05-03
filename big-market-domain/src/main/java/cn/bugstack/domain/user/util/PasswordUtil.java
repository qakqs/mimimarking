package cn.bugstack.domain.user.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Password utility using BCrypt for hashing and verification.
 */
public final class PasswordUtil {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private PasswordUtil() {}

    public static String encode(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }

}
