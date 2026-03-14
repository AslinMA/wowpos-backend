-- V10: Add date column to product table safely

ALTER TABLE product
ADD COLUMN IF NOT EXISTS date DATE;

UPDATE product
SET date = CURRENT_DATE
WHERE date IS NULL;