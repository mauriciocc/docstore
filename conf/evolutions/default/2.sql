# --- !Ups

DROP TABLE IF EXISTS database_file CASCADE ;
CREATE TABLE database_file (
  id           BIGSERIAL PRIMARY KEY,
  name         TEXT NOT NULL,
  content_type TEXT NOT NULL,
  content      BYTEA
);

CREATE INDEX ON database_file (id);


DROP TABLE IF EXISTS office CASCADE;
CREATE TABLE office (
  id              BIGSERIAL PRIMARY KEY,
  name            TEXT NOT NULL,
  organization_id BIGINT REFERENCES organization (id) ON DELETE CASCADE
);

CREATE INDEX ON office (id);
CREATE INDEX ON office (organization_id);


DROP TABLE IF EXISTS customer CASCADE;
CREATE TABLE customer (
  id        BIGSERIAL PRIMARY KEY,
  name      TEXT NOT NULL,
  office_id BIGINT REFERENCES office (id) ON DELETE CASCADE
);

CREATE INDEX ON customer (id);
CREATE INDEX ON customer (office_id);

DROP TABLE IF EXISTS document CASCADE;
CREATE TABLE document (
  id               BIGSERIAL PRIMARY KEY,
  name             TEXT NOT NULL,
  customer_id      BIGINT REFERENCES customer (id) ON DELETE CASCADE,
  database_file_id BIGINT REFERENCES database_file (id) ON DELETE SET NULL
);

CREATE INDEX ON document (id);
CREATE INDEX ON document (customer_id);
CREATE INDEX ON document (database_file_id);

# --- !Downs

DROP TABLE IF EXISTS database_file CASCADE ;
DROP TABLE IF EXISTS office CASCADE;
DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS document CASCADE;