-- V3: indexes and unique constraints (MySQL-safe, idempotent, and ≤ 3072 bytes)

-- ===== product: UNIQUE(category,brand,model,location) with prefixes to fit InnoDB 3072 bytes
SET @exists := (
  SELECT COUNT(1)
  FROM information_schema.statistics
  WHERE table_schema = DATABASE()
    AND table_name   = 'product'
    AND index_name   = 'uniq_product_cat_brand_model_loc'
);
SET @sql := IF(@exists = 0,
  -- use index prefixes so total key length < 3072 bytes (utf8mb4 worst-case)
  'CREATE UNIQUE INDEX uniq_product_cat_brand_model_loc ON product (category(100), brand(100), model(120), location(50))',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ===== product: ix_product_category
SET @exists := (
  SELECT COUNT(1) FROM information_schema.statistics
  WHERE table_schema = DATABASE() AND table_name='product' AND index_name='ix_product_category'
);
SET @sql := IF(@exists = 0,
  'CREATE INDEX ix_product_category ON product (category(100))',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ===== product: ix_product_brand
SET @exists := (
  SELECT COUNT(1) FROM information_schema.statistics
  WHERE table_schema = DATABASE() AND table_name='product' AND index_name='ix_product_brand'
);
SET @sql := IF(@exists = 0,
  'CREATE INDEX ix_product_brand ON product (brand(100))',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ===== product: ix_product_model
SET @exists := (
  SELECT COUNT(1) FROM information_schema.statistics
  WHERE table_schema = DATABASE() AND table_name='product' AND index_name='ix_product_model'
);
SET @sql := IF(@exists = 0,
  'CREATE INDEX ix_product_model ON product (model(120))',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ===== product: ix_product_is_active (TINYINT/BOOLEAN -> no prefix needed)
SET @exists := (
  SELECT COUNT(1) FROM information_schema.statistics
  WHERE table_schema = DATABASE() AND table_name='product' AND index_name='ix_product_is_active'
);
SET @sql := IF(@exists = 0,
  'CREATE INDEX ix_product_is_active ON product (is_active)',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ===== sale: ix_sale_date
SET @exists := (
  SELECT COUNT(1) FROM information_schema.statistics
  WHERE table_schema = DATABASE() AND table_name='sale' AND index_name='ix_sale_date'
);
SET @sql := IF(@exists = 0,
  'CREATE INDEX ix_sale_date ON sale (date)',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ===== sale: ix_sale_txn (transaction_id is likely VARCHAR; use a safe prefix)
SET @exists := (
  SELECT COUNT(1) FROM information_schema.statistics
  WHERE table_schema = DATABASE() AND table_name='sale' AND index_name='ix_sale_txn'
);
SET @sql := IF(@exists = 0,
  'CREATE INDEX ix_sale_txn ON sale (transaction_id(64))',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Note: sale.id is PRIMARY KEY already.
