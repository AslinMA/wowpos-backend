-- V6: requirement header table

CREATE TABLE IF NOT EXISTS requirement_list (
    id BIGSERIAL PRIMARY KEY,
    require_date DATE NOT NULL
);

ALTER TABLE requirement_item
ADD COLUMN IF NOT EXISTS requirement_id BIGINT NULL;

ALTER TABLE requirement_item
DROP CONSTRAINT IF EXISTS fk_requirement_item_header;

ALTER TABLE requirement_item
ADD CONSTRAINT fk_requirement_item_header
FOREIGN KEY (requirement_id) REFERENCES requirement_list(id);
