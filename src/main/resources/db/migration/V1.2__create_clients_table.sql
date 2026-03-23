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

-- Create sequence for clients
CREATE SEQUENCE clients_seq START WITH 1 INCREMENT BY 50;



