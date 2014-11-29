# --- !Ups
ALTER TABLE document DROP COLUMN IF EXISTS category_id;
DROP TABLE IF EXISTS category;

CREATE TABLE category (
  id   BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL
);
CREATE INDEX ON category (id);

INSERT INTO category (name) VALUES ('Sem categoria');


ALTER TABLE document ADD COLUMN category_id BIGINT REFERENCES category (id) ON DELETE SET DEFAULT DEFAULT 1;

# --- !Downs
ALTER TABLE document DROP COLUMN IF EXISTS category_id;
DROP TABLE IF EXISTS category;
