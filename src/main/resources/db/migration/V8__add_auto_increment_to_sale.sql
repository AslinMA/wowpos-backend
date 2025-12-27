-- V8: Convert sale.id to auto-increment (PostgreSQL)

CREATE SEQUENCE IF NOT EXISTS sale_id_seq;
ALTER TABLE sale ALTER COLUMN id SET DEFAULT nextval('sale_id_seq');
SELECT setval('sale_id_seq', COALESCE((SELECT MAX(id) FROM sale), 1));
