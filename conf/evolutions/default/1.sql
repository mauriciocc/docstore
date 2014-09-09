# --- !Ups

CREATE TABLE user (
  id           BIGSERIAL PRIMARY KEY,
  name         TEXT NOT NULL,
  display_name TEXT,
  email        TEXT NOT NULL,
  password     TEXT NOT NULL
);
/*CREATE INDEX ON user (id);
CREATE INDEX ON user (email);*/

# --- !Downs

DROP TABLE IF EXISTS user;