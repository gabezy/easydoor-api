ALTER TABLE appointments
    ADD COLUMN approved_user_id BIGINT,
    ADD COLUMN rejected_at TIMESTAMP;

ALTER TABLE hist_appointments
    ADD COLUMN approved_user_id BIGINT,
    ADD COLUMN rejected_at TIMESTAMP;

ALTER TABLE appointments
    ADD CONSTRAINT fk_appointments_approved_user
        FOREIGN KEY (approved_user_id) REFERENCES users(id);

CREATE INDEX idx_appointments_approved_user_id ON appointments(approved_user_id);
