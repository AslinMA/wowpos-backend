-- Create models table
CREATE TABLE models (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_brand_model (brand, model_name)
);

-- Insert existing models
INSERT INTO models (brand, model_name, category) VALUES
-- Apple models
('Apple', 'iPhone 6', 'Display'),
('Apple', 'iPhone 6s', 'Display'),
('Apple', 'iPhone 6 Plus', 'Display'),
('Apple', 'iPhone 7', 'Display'),
('Apple', 'iPhone 8', 'Display'),
('Apple', 'iPhone X', 'Display'),
('Apple', 'iPhone 11', 'Display'),
('Apple', 'iPhone 12', 'Display'),
('Apple', 'iPhone 13', 'Display'),
('Apple', 'iPhone 14', 'Display'),
('Apple', 'iPhone 15', 'Display'),
('Apple', 'Type C Cable', 'Charger'),
-- Samsung models
('Samsung', 'Galaxy S21', 'Display'),
('Samsung', 'Galaxy Note 10', 'Display'),
('Samsung', 'Galaxy A52', 'Display'),
('Samsung', 'Galaxy S20', 'Display'),
('Samsung', 'Galaxy A71', 'Display'),
('Samsung', 'Fast Charger', 'Charger'),
-- Redmi models
('Redmi', 'Note 9', 'Display'),
('Redmi', 'Note 10 Pro', 'Display'),
('Redmi', 'Redmi 12C', 'Display'),
('Redmi', 'Note 11', 'Display'),
('Redmi', 'Note 8 Pro', 'Display'),
-- Nokia models
('Nokia', 'Nokia 6.1', 'Display'),
('Nokia', 'Nokia 7.2', 'Display'),
('Nokia', 'Nokia 3.4', 'Display'),
('Nokia', 'Nokia 5.4', 'Display'),
('Nokia', 'Nokia 8.3', 'Display');

-- Add indexes
CREATE INDEX idx_brand ON models(brand);
CREATE INDEX idx_category ON models(category);
