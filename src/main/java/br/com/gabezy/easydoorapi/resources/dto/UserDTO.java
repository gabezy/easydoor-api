package br.com.gabezy.easydoorapi.resources.dto;

import br.com.gabezy.easydoorapi.domain.user.entities.User;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * User DTO for REST responses
 */
public record UserDTO (
        Long id,
        String username,
        String email,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLogin,
        Set<String> roleNames,
        Set<String> permissions
) {
    public UserDTO(User user) {
        this(user.getId(),
             user.getUsername(),
             user.getEmail(),
             user.isActive(),
             user.getCreatedAt(),
             user.getUpdatedAt(),
             user.getLastLogin(),
             user.getRoles().stream().map(role -> role.getName()).collect(java.util.stream.Collectors.toSet()),
             user.getRoles().stream()
                     .flatMap(role -> role.getPermissions().stream())
                     .map(permission -> permission.getCode())
                     .collect(java.util.stream.Collectors.toSet()));
    }
}

