package br.com.gabezy.easydoorapi.resources.dto;

import br.com.gabezy.easydoorapi.domain.user.entities.User;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Set;

@Schema(name = "User", description = "Detailed user representation")
public record UserDTO (
        @Schema(description = "User identifier", example = "1")
        Long id,
        @Schema(description = "Username", example = "admin")
        String username,
        @Schema(description = "E-mail address", example = "admin@easydoor.local")
        String email,
        @Schema(description = "Whether the user is active", example = "true")
        boolean active,
        @Schema(description = "Creation timestamp", example = "2026-04-14T20:00:00")
        LocalDateTime createdAt,
        @Schema(description = "Last update timestamp", example = "2026-04-14T20:05:00")
        LocalDateTime updatedAt,
        @Schema(description = "Last login timestamp", example = "2026-04-14T19:30:00")
        LocalDateTime lastLogin,
        @Schema(description = "Role names assigned to the user")
        Set<String> roleNames,
        @Schema(description = "Permission codes available to the user")
        Set<String> permissions
) {
    public UserDTO(User user) {
        this(user.id,
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
