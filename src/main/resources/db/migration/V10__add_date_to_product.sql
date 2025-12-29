-- Add date column to product table
ALTER TABLE product ADD COLUMN date DATE;

-- Optional: Set default value for existing records
UPDATE product SET date = CURRENT_DATE WHERE date IS NULL;
