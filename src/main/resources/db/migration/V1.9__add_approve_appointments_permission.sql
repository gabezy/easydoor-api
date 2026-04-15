INSERT INTO permissions (id, code, description, created_at) VALUES
(8, 'APPROVE_APPOINTMENTS', 'Approve or reject appointments', NOW());

INSERT INTO role_permissions (role_id, permission_id) VALUES
(1, 8);

INSERT INTO role_permissions (role_id, permission_id) VALUES
(2, 8);
