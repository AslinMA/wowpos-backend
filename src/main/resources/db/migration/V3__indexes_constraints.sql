-- V3: indexes and unique constraints (PostgreSQL version)

-- ===== product: UNIQUE(category,brand,model,location)
CREATE UNIQUE INDEX IF NOT EXISTS uniq_product_cat_brand_model_loc
ON product (category, brand, model, location);

-- ===== product: ix_product_category
CREATE INDEX IF NOT EXISTS ix_product_category
ON product (category);

-- ===== product: ix_product_brand
CREATE INDEX IF NOT EXISTS ix_product_brand
ON product (brand);

-- ===== product: ix_product_model
CREATE INDEX IF NOT EXISTS ix_product_model
ON product (model);

-- ===== product: ix_product_is_active
CREATE INDEX IF NOT EXISTS ix_product_is_active
ON product (is_active);

-- ===== sale: ix_sale_date
CREATE INDEX IF NOT EXISTS ix_sale_date
ON sale (date);

-- ===== sale: ix_sale_txn
CREATE INDEX IF NOT EXISTS ix_sale_txn
ON sale (transaction_id);
