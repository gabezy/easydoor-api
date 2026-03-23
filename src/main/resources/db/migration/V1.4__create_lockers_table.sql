-- V1.4__create_lockers_table.sql
-- Create lockers table (referenced by buildings)

CREATE TABLE lockers (
    id BIGINT NOT NULL,
    serial_number VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255),
    latitude NUMERIC(38,2),
    longitude NUMERIC(38,2),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    PRIMARY KEY (id)
);

-- Create sequence for lockers
CREATE SEQUENCE lockers_seq START WITH 1 INCREMENT BY 50;

-- Add locker_id column to buildings if it doesn't exist
ALTER TABLE buildings ADD COLUMN locker_id BIGINT;
ALTER TABLE buildings ADD CONSTRAINT fk_buildings_locker_id FOREIGN KEY (locker_id) REFERENCES lockers(id);



