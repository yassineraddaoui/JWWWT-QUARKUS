package org.acme.security;

import io.smallrye.jwt.build.Jwt;
import org.acme.model.User;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class JwtUtils {
    public static String generateJwtToken(User user) {
        Set<String> roles = new HashSet<>();
        roles.add(user.getRoles());

        return Jwt.issuer("https://example.com/issuer")
                .upn(user.getUsername()) // Username
                .groups(roles) // Roles
                .expiresIn(Duration.ofHours(1))
                .sign(); // Automatically uses privateKey.pem

    }
}
