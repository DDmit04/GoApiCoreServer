ALTER TABLE usr
    ADD user_api_tariff_id INTEGER;

ALTER TABLE usr
    ADD CONSTRAINT FK_USR_ON_USER_API_TARIFF FOREIGN KEY (user_api_tariff_id) REFERENCES user_api_tariff (id);

ALTER TABLE user_api
    DROP CONSTRAINT fk7va3mrikbo4f2gjns6m8d6116;

ALTER TABLE user_api
    DROP COLUMN api_tariff_id;

ALTER TABLE database
    ALTER COLUMN created_at DROP NOT NULL;

ALTER TABLE database_tariff
    ALTER COLUMN max_size_bytes DROP NOT NULL;

ALTER TABLE database
    ALTER COLUMN money_amount TYPE DECIMAL USING (money_amount::DECIMAL);

ALTER TABLE user_api
    ALTER COLUMN money_amount TYPE DECIMAL USING (money_amount::DECIMAL);

ALTER TABLE usr
    ALTER COLUMN money_amount TYPE DECIMAL USING (money_amount::DECIMAL);

ALTER TABLE user_api_request_argument
    ALTER COLUMN request_argument_type DROP NOT NULL;

ALTER TABLE payment
    ALTER COLUMN sum TYPE DECIMAL USING (sum::DECIMAL);