CREATE TABLE IF NOT EXISTS repair (
    repair_id SERIAL PRIMARY KEY,
    repair_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    customer_name VARCHAR(255),
    customer_phone VARCHAR(255),
    device_brand VARCHAR(255),
    device_model VARCHAR(255),
    issue_description TEXT,
    labor_charge DOUBLE PRECISION,
    total_cost DOUBLE PRECISION,
    status VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS repair_part (
    id SERIAL PRIMARY KEY,
    repair_id INTEGER,
    part_name VARCHAR(255),
    part_category VARCHAR(255),
    part_brand VARCHAR(255),
    part_model VARCHAR(255),
    quantity INTEGER,
    unit_price DOUBLE PRECISION,
    discount DOUBLE PRECISION,
    total_price DOUBLE PRECISION,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_repair_part_repair
        FOREIGN KEY (repair_id) REFERENCES repair(repair_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_repair_repair_date
    ON repair(repair_date);

CREATE INDEX IF NOT EXISTS idx_repair_status
    ON repair(status);

CREATE INDEX IF NOT EXISTS idx_repair_customer_phone
    ON repair(customer_phone);

CREATE INDEX IF NOT EXISTS idx_repair_part_repair_id
    ON repair_part(repair_id);

CREATE INDEX IF NOT EXISTS idx_repair_part_brand_model
    ON repair_part(part_brand, part_model);