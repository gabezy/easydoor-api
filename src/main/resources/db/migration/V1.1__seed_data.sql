-- V1.1__seed_data.sql
-- Insert initial data for permissions, roles, and test users

-- Insert permissions
INSERT INTO permissions (id, code, description, createdAt) VALUES
(1, 'USER_CREATE', 'Create new users', NOW()),
(2, 'USER_READ', 'Read user information', NOW()),
(3, 'USER_UPDATE', 'Update user information', NOW()),
(4, 'USER_DELETE', 'Delete users', NOW()),
(5, 'ROLE_MANAGE', 'Manage roles', NOW()),
(6, 'PERMISSION_MANAGE', 'Manage permissions', NOW());

-- Insert roles
INSERT INTO roles (id, name, description) VALUES
(1, 'ADMIN', 'Administrator with full access'),
(2, 'USER', 'Regular user with limited access'),
(3, 'GUEST', 'Guest user with read-only access');

-- Assign permissions to ADMIN role (all permissions)
INSERT INTO role_permissions (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6);

-- Assign permissions to USER role
INSERT INTO role_permissions (role_id, permission_id) VALUES
(2, 2), (2, 3);

-- Assign permissions to GUEST role
INSERT INTO role_permissions (role_id, permission_id) VALUES
(3, 2);

-- Insert test users with bcrypt-hashed passwords
-- Password for admin: admin123 (bcrypt hash)
-- Password for user: user123 (bcrypt hash)
-- Password for guest: guest123 (bcrypt hash)
INSERT INTO users (id, username, email, passwordHash, active) VALUES
(1, 'admin', 'admin@easydoor.local', '$2a$10$SlVZQbp0JY4zfVHi6mwDuOpst9/GyQ2L/PQcy/Ty1D5KwngWyOyti', true),
(2, 'user', 'user@easydoor.local', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36BCnvIm', true),
(3, 'guest', 'guest@easydoor.local', '$2a$10$V3zyD9/4SBqIVP0nHHEH2OPST9/pDHYiB5wK5X3Y8Z2L9N1mKsF5m', true);

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- admin has ADMIN role
(2, 2), -- user has USER role
(3, 3); -- guest has GUEST role

-- Reset sequences
ALTER SEQUENCE permissions_seq RESTART WITH 7;
ALTER SEQUENCE roles_seq RESTART WITH 4;
ALTER SEQUENCE users_seq RESTART WITH 4;

