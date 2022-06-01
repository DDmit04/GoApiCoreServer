
ALTER TABLE database
    DROP COLUMN password;

ALTER TABLE database
    ADD password BYTEA;