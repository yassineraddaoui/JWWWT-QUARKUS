package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "app_user")
@UserDefinition
@Builder
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class User extends PanacheEntity {

    @Username
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain letters, numbers, dots, underscores, and hyphens")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Password
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 8 characters long")
    private String password;

    @Email(message = "Must be a valid email address")
    @NotBlank(message = "Email cannot be blank")
    @Column(unique = true)
    private String email;
    @Roles
    @NotBlank(message = "Role cannot be blank")
    private String roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }

    public static Optional<User> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }

    public static Optional<User> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public boolean isTester() {
        return Objects.equals(roles, UserRole.TESTER.name());
    }

    public boolean isManager() {
        return Objects.equals(roles, UserRole.MANAGER.name());
    }

    public boolean isAdmin() {
        return Objects.equals(roles, UserRole.ADMIN.name());
    }
}