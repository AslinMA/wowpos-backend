CREATE TABLE IF NOT EXISTS return_record (
    return_id SERIAL PRIMARY KEY,
    return_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sale_transaction_id VARCHAR(255),
    original_sale_date DATE,
    customer_name VARCHAR(255),
    customer_phone VARCHAR(255),
    returned_category VARCHAR(255),
    returned_brand VARCHAR(255),
    returned_model VARCHAR(255),
    returned_quantity INTEGER,
    replacement_product_id INTEGER,
    replacement_category VARCHAR(255),
    replacement_brand VARCHAR(255),
    replacement_model VARCHAR(255),
    warranty_period VARCHAR(255),
    within_warranty BOOLEAN,
    claim_type VARCHAR(255),
    loss_amount DOUBLE PRECISION,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_return_record_return_date
    ON return_record(return_date);

CREATE INDEX IF NOT EXISTS idx_return_record_claim_type
    ON return_record(claim_type);

CREATE INDEX IF NOT EXISTS idx_return_record_sale_transaction_id
    ON return_record(sale_transaction_id);

CREATE INDEX IF NOT EXISTS idx_return_record_within_warranty
    ON return_record(within_warranty);