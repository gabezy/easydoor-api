package br.com.gabezy.easydoorapi.domain.user.repositories;

import br.com.gabezy.easydoorapi.domain.user.entities.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.Optional;

public interface UserRepository extends PanacheRepository<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameWithRoles(String username);

    Optional<User> findByIdWithRoles(Long id);

}
