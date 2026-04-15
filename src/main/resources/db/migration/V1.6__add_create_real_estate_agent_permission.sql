INSERT INTO permissions (id, code, description, created_at) VALUES
(6, 'CREATE_REAL_ESTATE_AGENT', 'Create new real estate agent', NOW());

INSERT INTO role_permissions (role_id, permission_id) VALUES
(1, 6);
