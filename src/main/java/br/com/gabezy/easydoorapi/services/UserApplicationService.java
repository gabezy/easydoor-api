package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.user.entities.Role;
import br.com.gabezy.easydoorapi.domain.user.entities.User;
import br.com.gabezy.easydoorapi.infra.repositories.RoleRepository;
import br.com.gabezy.easydoorapi.infra.repositories.UserRepository;
import br.com.gabezy.easydoorapi.resources.dto.UserDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserApplicationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserApplicationService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.listAll().stream()
                .map(this::mapUserToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long userId) {
        User user = userRepository.findByIdWithRoles(userId);
        return user != null ? mapUserToDTO(user) : null;
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsernameWithRoles(username);
        return user != null ? mapUserToDTO(user) : null;
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Transactional
    public UserDTO updateUser(Long userId, String email, boolean active) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return null;
        }

        // Email can be updated if not taken by another user
        if (email != null && !email.equals(user.email)) {
            User existingUser = userRepository.findByEmail(email);
            if (existingUser != null) {
                throw new IllegalArgumentException("Email already in use");
            }
            user.email = email;
        }

        user.active = active;
        userRepository.persist(user);
        return mapUserToDTO(user);
    }

    @Transactional
    public void assignRoleToUser(Long userId, String roleName) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Role role = roleRepository.findByNameWithPermissions(roleName);
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }

        user.addRole(role);
        userRepository.persist(user);
    }

    @Transactional
    public void removeRoleFromUser(Long userId, String roleName) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }

        user.removeRole(role);
        userRepository.persist(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.active = false;
            userRepository.persist(user);
        }
    }

    private UserDTO mapUserToDTO(User user) {
        return new UserDTO(
                user.id,
                user.username,
                user.email,
                user.active,
                user.createdAt,
                user.updatedAt,
                user.lastLogin,
                user.roles.stream().map(r -> r.name).collect(Collectors.toSet()),
                user.roles.stream()
                        .flatMap(r -> r.permissions.stream())
                        .map(p -> p.code)
                        .collect(Collectors.toSet())
        );
    }
}

