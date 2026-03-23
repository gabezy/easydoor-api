-- V1.2__create_clients_table.sql
-- Create clients table for client user profiles

CREATE TABLE clients (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) UNIQUE,
    user_id BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE hist_clients(
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) UNIQUE,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    REV INTEGER NOT NULL,
    REVTYPE INTEGER,
    PRIMARY KEY (id, REV),
    FOREIGN KEY (REV) REFERENCES REVINFO(REV)
);

-- Create sequence for clients
CREATE SEQUENCE clients_seq START WITH 1 INCREMENT BY 50;



