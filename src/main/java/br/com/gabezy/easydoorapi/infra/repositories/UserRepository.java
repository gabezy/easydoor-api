package br.com.gabezy.easydoorapi.infra.repositories;

import br.com.gabezy.easydoorapi.domain.user.entities.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository for User entities using Panache
 */
@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public User findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public User findByUsernameWithRoles(String username) {
        return find("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = ?1", username)
                .firstResult();
    }

    public User findByIdWithRoles(Long id) {
        return find("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = ?1", id)
                .firstResult();
    }
}

