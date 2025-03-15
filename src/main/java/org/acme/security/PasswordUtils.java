package org.acme.security;

import io.quarkus.elytron.security.common.BcryptUtil;

public class PasswordUtils {
    public static boolean verifyPassword(String password, String hashedPassword) {
        return BcryptUtil.matches(password, hashedPassword);
    }

    public static String hashPassword(String password) {
        return BcryptUtil.bcryptHash(password);
    }
}
