--- SEQUENCES AND TABLES

CREATE SEQUENCE IF NOT EXISTS permissions_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS refresh_tokens_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS roles_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE permissions
(
    id          BIGINT       NOT NULL,
    code        VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    createdAt   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updatedAt   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_permissions PRIMARY KEY (id)
);

CREATE TABLE refresh_tokens
(
    id        BIGINT NOT NULL,
    token     TEXT   NOT NULL,
    userId    BIGINT NOT NULL,
    expiresAt TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    revokedAt TIMESTAMP WITHOUT TIME ZONE,
    createdAt TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id)
);

CREATE TABLE role_permissions
(
    permission_id BIGINT NOT NULL,
    role_id       BIGINT NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (permission_id, role_id)
);

CREATE TABLE roles
(
    id          BIGINT       NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    createdAt   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updatedAt   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (role_id, user_id)
);

CREATE TABLE users
(
    id           BIGINT       NOT NULL,
    username     VARCHAR(100) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    passwordHash VARCHAR(255) NOT NULL,
    active       BOOLEAN      NOT NULL,
    createdAt    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updatedAt    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    lastLogin    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

--- CONSTRAINTS

ALTER TABLE permissions
    ADD CONSTRAINT uc_permissions_code UNIQUE (code);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT uc_refresh_tokens_token UNIQUE (token);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permission_id) REFERENCES permissions (id);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);