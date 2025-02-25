-- Insert admin user
INSERT INTO users (username, email, password_hash, role, status, created_at, updated_at)
VALUES (
    'admin',
    'admin@example.com',
    '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', -- password: admin123
    'ADMIN',
    'ACTIVE',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT DO NOTHING;

-- Insert test sales manager
INSERT INTO users (username, email, password_hash, role, status, created_at, updated_at)
VALUES (
    'manager',
    'manager@example.com',
    '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', -- password: admin123
    'SALES_MANAGER',
    'ACTIVE',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT DO NOTHING;

-- Insert test customers
INSERT INTO customers (name, company, title, email, phone, address, status, category, created_at, updated_at, created_by)
VALUES
    ('John Doe', 'Acme Corp', 'CEO', 'john@acme.com', '+1234567890', '123 Business St', 'ACTIVE', 'PREMIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('Jane Smith', 'Tech Inc', 'CTO', 'jane@tech.com', '+1987654321', '456 Tech Ave', 'ACTIVE', 'ENTERPRISE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1)
ON CONFLICT DO NOTHING;

-- Insert test contacts
INSERT INTO contacts (customer_id, name, position, email, phone, communication_preference, created_at, updated_at, created_by)
VALUES
    (1, 'Alice Johnson', 'Sales Director', 'alice@acme.com', '+1122334455', 'EMAIL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    (1, 'Bob Wilson', 'Marketing Manager', 'bob@acme.com', '+1122334466', 'PHONE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    (2, 'Carol Brown', 'Project Manager', 'carol@tech.com', '+1122334477', 'BOTH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1)
ON CONFLICT DO NOTHING;