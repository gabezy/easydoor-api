package br.com.gabezy.easydoorapi.domain.user.services;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Domain Service for password hashing using bcrypt
 */
@ApplicationScoped
public class PasswordHashingService {

    /**
     * Hash a plain password using bcrypt
     *
     * @param plainPassword the plain password to hash
     * @return bcrypt hashed password
     */
    public String hashPassword(String plainPassword) {
        return BcryptUtil.bcryptHash(plainPassword);
    }

    /**
     * Verify a plain password against a bcrypt hash
     *
     * @param plainPassword the plain password to verify
     * @param hashedPassword the bcrypt hash to verify against
     * @return true if password matches, false otherwise
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BcryptUtil.matches(plainPassword, hashedPassword);
    }
}


