-- V7: Convert product.id to auto-increment (PostgreSQL)
-- PostgreSQL: Assuming id already exists as BIGINT, convert to BIGSERIAL behavior

CREATE SEQUENCE IF NOT EXISTS product_id_seq;
ALTER TABLE product ALTER COLUMN id SET DEFAULT nextval('product_id_seq');
SELECT setval('product_id_seq', COALESCE((SELECT MAX(id) FROM product), 1));
