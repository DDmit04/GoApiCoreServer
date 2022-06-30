ALTER TABLE user_api_request_argument
    ADD user_api_request_id INTEGER;

ALTER TABLE user_api_request_argument
    ADD CONSTRAINT FK_USERAPIREQUESTARGUMENT_ON_USER_API_REQUEST FOREIGN KEY (user_api_request_id) REFERENCES user_api_request (id);

ALTER TABLE user_api_request_argument
    DROP CONSTRAINT fk_userapirequestargument_on_api_request;

ALTER TABLE user_api_request_argument
    DROP COLUMN api_request_id;