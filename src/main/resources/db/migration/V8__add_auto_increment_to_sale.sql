-- V8: Convert sale.id to auto-increment (PostgreSQL)

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name='sale'
        AND column_name='id'
    ) THEN

        CREATE SEQUENCE IF NOT EXISTS sale_id_seq;

        ALTER TABLE sale
        ALTER COLUMN id SET DEFAULT nextval('sale_id_seq');

        PERFORM setval('sale_id_seq', COALESCE((SELECT MAX(id) FROM sale), 1));

    END IF;
END $$;