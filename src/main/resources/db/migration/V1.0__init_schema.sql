-- V1.0__init_schema.sql
-- Initial database schema creation

CREATE TABLE buildings (
    id BIGINT NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    zipCode VARCHAR(255) NOT NULL,
    area FLOAT NOT NULL,
    description VARCHAR(255) NOT NULL,
    latitude NUMERIC(38,2) NOT NULL,
    longitude NUMERIC(38,2) NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE permissions (
    id BIGINT NOT NULL,
    code VARCHAR(100) NOT NULL UNIQUE,
    createdAt TIMESTAMP NOT NULL,
    description VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE refresh_tokens (
    id BIGINT NOT NULL,
    expiresAt TIMESTAMP NOT NULL,
    revokedAt TIMESTAMP,
    token TEXT NOT NULL UNIQUE,
    userId BIGINT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE roles (
    id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE users (
    id BIGINT NOT NULL,
    active BOOLEAN NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    lastLogin TIMESTAMP,
    passwordHash VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create sequences
CREATE SEQUENCE buildings_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE permissions_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE refresh_tokens_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE roles_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 50;

