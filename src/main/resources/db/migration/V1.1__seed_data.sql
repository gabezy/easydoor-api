-- V1.1__seed_data.sql
-- Insert initial data for permissions, roles, and test users

-- Insert permissions
INSERT INTO permissions (id, code, description, created_at) VALUES
( 1, 'VIEW_USERS', 'Read user information', NOW()),
( 2, 'CREATE_APPOINTMENT', 'Create new appointment', NOW()),
( 3, 'VIEW_APPOINTMENT', 'View appointment', NOW()),
( 4, 'VIEW_APPOINTMENTS', 'View appointments', NOW()),
( 5, 'CREATE_BUILDING', 'Create new building', NOW())
;

-- Insert roles
INSERT INTO roles (id, name, description, created_at) VALUES
(1, 'ADMIN', 'Administrator with full access', now()),
(2, 'AGENT', 'Real estate agent', now()),
(3, 'CLIENT', 'Client', now());

-- Assign permissions to ADMIN role
INSERT INTO role_permissions (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5);

-- Assign permissions to AGENT role
INSERT into role_permissions (role_id, permission_id) VALUES
(2, 3), (2, 4), (2, 5);

-- Assign permissions to USER role
INSERT INTO role_permissions (role_id, permission_id) VALUES
(3, 3), (3, 4);


-- Insert test users with bcrypt-hashed passwords
-- Password for admin: admin123 (bcrypt hash)
-- Password for guest: agent123 (bcrypt hash)
-- Password for user: client123 (bcrypt hash)
INSERT INTO users (id, username, email, password_hash, active, created_at) VALUES
(1, 'admin', 'admin@easydoor.local', '$2a$12$e0ogP5w3.v7JB7l/zvPAiueRSCpeIjbUuoXy312rYcR.TBHcIPc9.', true, now()),
(2, 'agent', 'user@easydoor.local', '$2a$12$7rt7UPj4gehvtbM.459WcONmu7imiCpVUYjbQrmC1DqZbb2eKozry', true, now()),
(3, 'client', 'guest@easydoor.local', '$2a$12$YAU6VyWd0WfcEx0nFNDbVeWlu6BcG3V067.6CSXCIxYxBWNJ2325m', true, now());

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- admin has ADMIN role
(2, 2), -- user has USER role
(3, 3); -- guest has GUEST role

-- Reset sequences
ALTER SEQUENCE permissions_seq RESTART WITH 7;
ALTER SEQUENCE roles_seq RESTART WITH 4;
ALTER SEQUENCE users_seq RESTART WITH 4;

