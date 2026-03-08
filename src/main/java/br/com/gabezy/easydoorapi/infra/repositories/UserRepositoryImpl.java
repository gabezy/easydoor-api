package br.com.gabezy.easydoorapi.infra.repositories;

import br.com.gabezy.easydoorapi.domain.user.entities.User;
import br.com.gabezy.easydoorapi.domain.user.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {

    public Optional<User> findByUsername(String username) {
        return find("username", username).singleResultOptional();
    }

    public Optional<User> findByEmail(String email) {
        return find("email", email).singleResultOptional();
    }

    public Optional<User> findByUsernameWithRoles(String username) {
        return find("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = ?1", username)
                .singleResultOptional();
    }

    public Optional<User> findByIdWithRoles(Long id) {
        return find("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = ?1", id)
                .singleResultOptional();
    }
}

