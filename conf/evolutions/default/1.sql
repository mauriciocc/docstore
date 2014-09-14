# --- !Ups

CREATE TABLE user_account (
  id           BIGSERIAL PRIMARY KEY,
  name         TEXT NOT NULL,
  display_name TEXT,
  email        TEXT NOT NULL,
  password     TEXT NOT NULL
);

CREATE TABLE account (
  id       BIGSERIAL PRIMARY KEY,
  name     TEXT NOT NULL,
  owner_id BIGINT REFERENCES user_account (id)
);

CREATE TABLE organization (
  id   BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  account_id BIGINT REFERENCES account (id)
);

# --- !Downs

DROP TABLE IF EXISTS organization;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS user_account;