ALTER TABLE user_api
    ADD user_api_tariff_id INTEGER;

ALTER TABLE user_api
    ADD CONSTRAINT FK_USERAPI_ON_USER_API_TARIFF FOREIGN KEY (user_api_tariff_id) REFERENCES user_api_tariff (id);

ALTER TABLE usr
    DROP CONSTRAINT fk9d0gho9svbjpw74efg2js8sc4;

ALTER TABLE usr
    DROP COLUMN user_api_tariff_id;

ALTER TABLE database
    ALTER COLUMN created_at DROP NOT NULL;

ALTER TABLE user_api
    ALTER COLUMN created_at DROP NOT NULL;

ALTER TABLE database_tariff
    ALTER COLUMN max_size_bytes DROP NOT NULL;

ALTER TABLE user_api_request_argument
    ALTER COLUMN request_argument_type DROP NOT NULL;

ALTER TABLE app_service_bill_payment
    ALTER COLUMN sum TYPE DECIMAL USING (sum::DECIMAL);

ALTER TABLE user_bill_payment
    ALTER COLUMN sum TYPE DECIMAL USING (sum::DECIMAL);