CREATE TABLE IF NOT EXISTS requirement_list (
  id           BIGINT PRIMARY KEY AUTO_INCREMENT,
  require_date DATE NOT NULL
);

ALTER TABLE requirement_item
  ADD COLUMN requirement_id BIGINT NULL,
  ADD CONSTRAINT fk_requirement_item_header
    FOREIGN KEY (requirement_id) REFERENCES requirement_list(id);
