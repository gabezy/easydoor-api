package br.com.gabezy.easydoorapi.resources.dto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * User DTO for REST responses
 */
public class UserDTO {
    public Long id;
    public String username;
    public String email;
    public boolean active;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public LocalDateTime lastLogin;
    public Set<String> roleNames;
    public Set<String> permissions;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, boolean active,
                   LocalDateTime createdAt, LocalDateTime updatedAt,
                   LocalDateTime lastLogin, Set<String> roleNames, Set<String> permissions) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLogin = lastLogin;
        this.roleNames = roleNames;
        this.permissions = permissions;
    }
}

