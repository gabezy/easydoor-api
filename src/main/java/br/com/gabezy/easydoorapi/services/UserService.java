package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.role.entities.Role;
import br.com.gabezy.easydoorapi.domain.user.entities.User;
import br.com.gabezy.easydoorapi.infra.repositories.RoleRepositoryImpl;
import br.com.gabezy.easydoorapi.infra.repositories.UserRepositoryImpl;
import br.com.gabezy.easydoorapi.resources.dto.UserDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    private final UserRepositoryImpl userRepositoryImpl;
    private final RoleRepositoryImpl roleRepositoryImpl;

    public UserService(UserRepositoryImpl userRepositoryImpl, RoleRepositoryImpl roleRepositoryImpl) {
        this.userRepositoryImpl = userRepositoryImpl;
        this.roleRepositoryImpl = roleRepositoryImpl;
    }

    public List<UserDTO> getAllUsers() {
        return userRepositoryImpl.listAll().stream()
                .map(this::mapUserToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long userId) {
        return userRepositoryImpl.findByIdWithRoles(userId)
                .map(this::mapUserToDTO)
                .orElse(null);
    }

    public UserDTO getUserByUsername(String username) {
        return userRepositoryImpl.findByUsernameWithRoles(username)
                .map(this::mapUserToDTO)
                .orElse(null);
    }

    public boolean usernameExists(String username) {
        return userRepositoryImpl.findByUsername(username) != null;
    }

    public boolean emailExists(String email) {
        return userRepositoryImpl.findByEmail(email) != null;
    }

    @Transactional
    public UserDTO updateUser(Long userId, String email, boolean active) {
        User user = userRepositoryImpl.findById(userId);
        if (user == null) {
            return null;
        }

        if (email != null && !email.equals(user.getEmail())) {
            userRepositoryImpl.findByEmail(email)
                    .orElseThrow();
            user.setEmail(email);
        }

        user.setActive(active);
        userRepositoryImpl.persist(user);
        return mapUserToDTO(user);
    }

    @Transactional
    public void assignRoleToUser(Long userId, String roleName) {
        User user = userRepositoryImpl.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Role role = roleRepositoryImpl.findByNameWithPermissions(roleName)
                .orElseThrow();

        user.addRole(role);
        userRepositoryImpl.persist(user);
    }

    @Transactional
    public void removeRoleFromUser(Long userId, String roleName) {
        User user = userRepositoryImpl.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Role role = roleRepositoryImpl.findByName(roleName)
                .orElseThrow();

        user.removeRole(role);
        userRepositoryImpl.persist(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepositoryImpl.findById(userId);
        if (user != null) {
            user.setActive(false);
            userRepositoryImpl.persist(user);
        }
    }

    private UserDTO mapUserToDTO(User user) {
        return new UserDTO(user);
    }
}

