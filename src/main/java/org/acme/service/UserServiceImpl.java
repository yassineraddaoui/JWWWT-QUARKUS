package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.dto.LoginDto;
import org.acme.dto.RegisterDto;
import org.acme.dto.TokenResponse;
import org.acme.exception.UserAlreadyExistsException;
import org.acme.exception.UserNotFoundException;
import org.acme.exception.WrongCredentialsException;
import org.acme.model.User;
import org.acme.security.JwtUtils;
import org.acme.security.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Transactional
    public TokenResponse register(RegisterDto registerDto) {
        if (registerDto == null) {
            LOG.error("Register data is null");
            throw new IllegalArgumentException("Register data must not be null");
        }

        LOG.info("Attempting to register user: {}", registerDto.getUsername());

        if (User.findByUsername(registerDto.getUsername()).isPresent()) {
            LOG.warn("User already exists: {}", registerDto.getUsername());
            throw new UserAlreadyExistsException("Username " + registerDto.getUsername() + " already exists");
        }

        var newUser = User.builder()
                .password(PasswordUtils.hashPassword(registerDto.getPassword()))
                .name(registerDto.getName())
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .roles("user")
                .build();
        newUser.persist();

        LOG.info("User registered successfully: {}", registerDto.getUsername());

        return TokenResponse.builder()
                .message("Register Successfully")
                .token(JwtUtils.generateJwtToken(newUser))
                .build();
    }

    public TokenResponse signIn(LoginDto user) {
        if (user == null) {
            LOG.error("Login data is null");
            throw new IllegalArgumentException("Login data must not be null");
        }

        LOG.info("Attempting to sign in user: {}", user.getUsername());

        User existingUser = User.findByUsername(user.getUsername())
                .orElseThrow(() -> {
                    LOG.warn("User not found: {}", user.getUsername());
                    return new UserNotFoundException("Username " + user.getUsername() + " not found");
                });

        if (!PasswordUtils.verifyPassword(user.getPassword(), existingUser.getPassword())) {
            LOG.warn("Failed login attempt for username: {}", user.getUsername());
            throw new WrongCredentialsException("Invalid password");
        }

        LOG.info("User signed in successfully: {}", user.getUsername());

        return TokenResponse
                .builder()
                .token(JwtUtils.generateJwtToken(existingUser))
                .message("Successfully logged in")
                .build();
    }

    public User getCurrentUser(SecurityContext context) {
        String username = context.getUserPrincipal().getName();
        return User.findByUsername(username)
                .orElseThrow(() -> new SecurityException("User not found"));
    }

    @Override
    public Optional<User> findById(Long id) {
        return User.findByIdOptional(id);
    }

}