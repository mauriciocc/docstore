# --- !Ups

ALTER TABLE document DROP COLUMN IF EXISTS creation_date;
ALTER TABLE document DROP COLUMN IF EXISTS due_date;
ALTER TABLE document ADD COLUMN due_date DATE;

DROP TABLE IF EXISTS notification;

CREATE TABLE notification (
  id         BIGSERIAL PRIMARY KEY,
  message    TEXT      NOT NULL,
  created_at TIMESTAMP NOT NULL,
  read_at    TIMESTAMP,
  user_id    BIGINT    NOT NULL REFERENCES user_account (id) on DELETE CASCADE
);

CREATE INDEX ON notification (id);
CREATE INDEX ON notification (user_id);


DROP TABLE IF EXISTS database_file_download;
CREATE TABLE database_file_download (
  id               BIGSERIAL PRIMARY KEY,
  downloaded_at    TIMESTAMP NOT NULL,
  database_file_id BIGINT    NOT NULL REFERENCES database_file (id) on DELETE CASCADE,
  user_id          BIGINT    NOT NULL REFERENCES user_account (id) on DELETE SET NULL
);

CREATE INDEX ON database_file_download (id);
CREATE INDEX ON database_file_download (database_file_id);
CREATE INDEX ON database_file_download (user_id);

ALTER TABLE database_file DROP COLUMN IF EXISTS size;
ALTER TABLE database_file ADD COLUMN size BIGINT DEFAULT 0;

ALTER TABLE document DROP COLUMN IF EXISTS created_at;
ALTER TABLE document ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT now();

# --- !Downs

ALTER TABLE document DROP COLUMN IF EXISTS creation_date;
ALTER TABLE document DROP COLUMN IF EXISTS due_date;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS database_file_download;