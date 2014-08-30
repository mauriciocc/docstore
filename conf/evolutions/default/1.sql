# --- !Ups

CREATE TABLE user (
  id       BIGSERIAL PRIMARY KEY,
  name     TEXT,
  email    TEXT,
  password TEXT
);

# --- !Downs

DROP TABLE IF EXISTS user CASCADE;