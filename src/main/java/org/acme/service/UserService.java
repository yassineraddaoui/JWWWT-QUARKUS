package org.acme.service;

import java.util.Optional;

import org.acme.dto.LoginDto;
import org.acme.dto.RegisterDto;
import org.acme.dto.TokenResponse;
import org.acme.model.User;

import jakarta.ws.rs.core.SecurityContext;
public interface UserService {
    User getCurrentUser(SecurityContext context);
    Optional<User> findById(Long id);
    TokenResponse register(RegisterDto registerDto);
    TokenResponse signIn(LoginDto user);
}
