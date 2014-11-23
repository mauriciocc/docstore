# --- !Ups
ALTER TABLE account RENAME owner_id TO user_id;

# --- !Downs
ALTER TABLE account RENAME user_id TO owner_id;
