-- V1.3__create_real_estate_agents_table.sql
-- Create real estate agents table for real estate agent user profiles

CREATE TABLE real_estate_agents (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    cnpj VARCHAR(14) UNIQUE,
    creci VARCHAR(20),
    phone VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(2),
    country VARCHAR(255),
    zip_code VARCHAR(10),
    user_id BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE hist_real_estate_agents (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    cnpj VARCHAR(14) UNIQUE,
    creci VARCHAR(20),
    phone VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(2),
    country VARCHAR(255),
    zip_code VARCHAR(10),
    user_id BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    REV INTEGER NOT NULL,
    REVTYPE INTEGER,
    PRIMARY KEY (id, REV),
    FOREIGN KEY (REV) REFERENCES revinfo(REV)
);

-- Create sequence for real estate agents
CREATE SEQUENCE real_estate_agents_seq START WITH 1 INCREMENT BY 50;