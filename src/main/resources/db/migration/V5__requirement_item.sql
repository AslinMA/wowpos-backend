-- V5__requirement_item.sql

CREATE TABLE IF NOT EXISTS requirement_item (
    id BIGSERIAL PRIMARY KEY,
    req_date DATE NOT NULL,
    category VARCHAR(100) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(150) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(12,2) NULL,
    notes VARCHAR(1000) NULL
);

CREATE INDEX IF NOT EXISTS idx_requirement_item_req_date ON requirement_item (req_date);
CREATE INDEX IF NOT EXISTS idx_requirement_item_brand_model ON requirement_item (brand, model);
