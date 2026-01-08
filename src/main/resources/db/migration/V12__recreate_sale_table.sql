-- V12: Recreate sale table with all required columns for warranty system

-- Drop existing sale table if it exists (and all its dependencies)
DROP TABLE IF EXISTS sale CASCADE;

-- Create new sale table with complete schema
CREATE TABLE sale (
    sale_id SERIAL PRIMARY KEY,
    transaction_id VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    date DATE NOT NULL,
    product_id INTEGER,
    category VARCHAR(100),
    brand VARCHAR(100),
    model VARCHAR(100),
    quantity INTEGER NOT NULL,
    sell_price DECIMAL(10,2) NOT NULL,
    discounted_price DECIMAL(10,2),
    warranty_period VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add foreign key constraint
ALTER TABLE sale ADD CONSTRAINT fk_sale_product
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE SET NULL;

-- Create indexes for performance
CREATE INDEX idx_sale_transaction_id ON sale(transaction_id);
CREATE INDEX idx_sale_phone_number ON sale(phone_number);
CREATE INDEX idx_sale_name ON sale(name);
CREATE INDEX idx_sale_date ON sale(date DESC);
CREATE INDEX idx_sale_product_id ON sale(product_id);

-- Add comment for documentation
COMMENT ON TABLE sale IS 'Sales records with warranty tracking';
COMMENT ON COLUMN sale.transaction_id IS 'Unique transaction identifier for warranty claims';
COMMENT ON COLUMN sale.discounted_price IS 'Final price after discount';
COMMENT ON COLUMN sale.warranty_period IS 'Warranty duration (e.g., "1 Year")';
