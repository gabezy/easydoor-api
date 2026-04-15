INSERT INTO permissions (id, code, description, created_at) VALUES
(7, 'VIEW_REAL_ESTATE_AGENTS', 'View real estate agents', NOW());

INSERT INTO role_permissions (role_id, permission_id) VALUES
(1, 7);
