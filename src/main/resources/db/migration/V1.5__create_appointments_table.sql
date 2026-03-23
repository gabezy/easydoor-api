-- V1.5__create_appointments_table.sql
-- Create appointments table for scheduling building visits

CREATE TABLE appointments (
    id BIGINT NOT NULL,
    time TIMESTAMP NOT NULL,
    client_id BIGINT NOT NULL,
    real_estate_agent_id BIGINT NOT NULL,
    building_id BIGINT NOT NULL,
    canceled_at TIMESTAMP,
    approved_at TIMESTAMP,
    finished_at TIMESTAMP,
    rating INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    FOREIGN KEY (real_estate_agent_id) REFERENCES real_estate_agents(id) ON DELETE CASCADE,
    FOREIGN KEY (building_id) REFERENCES buildings(id) ON DELETE CASCADE
);

CREATE TABLE hist_appointments (
    id BIGINT NOT NULL,
    time TIMESTAMP NOT NULL,
    client_id BIGINT NOT NULL,
    real_estate_agent_id BIGINT NOT NULL,
    building_id BIGINT NOT NULL,
    canceled_at TIMESTAMP,
    approved_at TIMESTAMP,
    finished_at TIMESTAMP,
    rating INT DEFAULT 0,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    REV INTEGER NOT NULL,
    REVTYPE INTEGER,
    PRIMARY KEY (id, REV),
    FOREIGN KEY (REV) REFERENCES REVINFO(REV)
);

-- Create sequence for appointments
CREATE SEQUENCE appointments_seq START WITH 1 INCREMENT BY 50;

-- Create index for better query performance
CREATE INDEX idx_appointments_client_id ON appointments(client_id);
CREATE INDEX idx_appointments_agent_id ON appointments(real_estate_agent_id);
CREATE INDEX idx_appointments_building_id ON appointments(building_id);
CREATE INDEX idx_appointments_time ON appointments(time);



