-- V1: Create initial product and sale tables

CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY,
    category VARCHAR(100) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(150) NOT NULL,
    location VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    buy_price DECIMAL(12,2) NOT NULL,
    sell_price DECIMAL(12,2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS sale (
    id BIGINT PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    transaction_id VARCHAR(100) NOT NULL,
    category VARCHAR(100) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(150) NOT NULL,
    quantity INT NOT NULL,
    sell_price DECIMAL(12,2) NOT NULL
);
