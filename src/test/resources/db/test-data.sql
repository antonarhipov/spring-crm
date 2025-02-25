-- Test Users
INSERT INTO users (id, username, email, password_hash, role, status, created_at, updated_at)
VALUES
    (1, 'admin', 'admin@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'ADMIN', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'manager', 'manager@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'SALES_MANAGER', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'sales_rep', 'sales@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'SALES_REPRESENTATIVE', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 'read_only', 'reader@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'READ_ONLY', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Test Customers
INSERT INTO customers (id, name, company, title, email, phone, address, status, category, created_at, updated_at, created_by)
VALUES
    (1, 'John Doe', 'Acme Corp', 'CEO', 'john@acme.com', '+1234567890', '123 Business St', 'ACTIVE', 'PREMIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
    (2, 'Jane Smith', 'Tech Inc', 'CTO', 'jane@tech.com', '+1987654321', '456 Tech Ave', 'ACTIVE', 'ENTERPRISE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
    (3, 'Bob Wilson', 'Local Shop', 'Owner', 'bob@local.com', '+1122334455', '789 Local St', 'ACTIVE', 'REGULAR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3)
ON CONFLICT (id) DO NOTHING;

-- Test Contacts
INSERT INTO contacts (id, customer_id, name, position, email, phone, communication_preference, created_at, updated_at, created_by)
VALUES
    (1, 1, 'Alice Johnson', 'Sales Director', 'alice@acme.com', '+1111111111', 'EMAIL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
    (2, 1, 'Charlie Brown', 'Marketing Manager', 'charlie@acme.com', '+2222222222', 'PHONE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
    (3, 2, 'David Miller', 'Project Manager', 'david@tech.com', '+3333333333', 'BOTH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3),
    (4, 3, 'Eve Wilson', 'Assistant', 'eve@local.com', '+4444444444', 'EMAIL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3)
ON CONFLICT (id) DO NOTHING;

-- Reset sequence values
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('customers_id_seq', (SELECT MAX(id) FROM customers));
SELECT setval('contacts_id_seq', (SELECT MAX(id) FROM contacts));