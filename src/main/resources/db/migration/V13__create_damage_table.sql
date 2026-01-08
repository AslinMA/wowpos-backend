-- V13: Create damage tracking table

CREATE TABLE damage (
    damage_id SERIAL PRIMARY KEY,
    damage_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    product_id INTEGER NOT NULL,
    category VARCHAR(100) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    damage_type VARCHAR(50) NOT NULL,
    damage_reason TEXT,
    buy_price DECIMAL(10,2) NOT NULL,
    loss_amount DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_damage_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_damage_date ON damage(damage_date DESC);
CREATE INDEX idx_damage_product_id ON damage(product_id);
CREATE INDEX idx_damage_type ON damage(damage_type);

-- Add comments
COMMENT ON TABLE damage IS 'Records of damaged inventory with loss tracking';
