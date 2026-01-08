-- Add missing columns to sale table
ALTER TABLE sale ADD COLUMN IF NOT EXISTS product_id INTEGER;
ALTER TABLE sale ADD COLUMN IF NOT EXISTS discounted_price DECIMAL(10,2);
ALTER TABLE sale ADD COLUMN IF NOT EXISTS warranty_period VARCHAR(50);
ALTER TABLE sale ADD COLUMN IF NOT EXISTS transaction_id VARCHAR(100);

-- Add foreign key if product_id doesn't have it
ALTER TABLE sale ADD CONSTRAINT fk_sale_product
    FOREIGN KEY (product_id) REFERENCES product(id)
    ON DELETE SET NULL;

-- Create index for faster lookups
CREATE INDEX IF NOT EXISTS idx_sale_transaction_id ON sale(transaction_id);
CREATE INDEX IF NOT EXISTS idx_sale_product_id ON sale(product_id);
