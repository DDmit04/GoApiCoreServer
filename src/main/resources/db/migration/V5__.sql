ALTER TABLE user_api_request_argument
    ADD api_request_id INTEGER;

ALTER TABLE user_api_request_argument
    ADD CONSTRAINT FK_USERAPIREQUESTARGUMENT_ON_API_REQUEST FOREIGN KEY (api_request_id) REFERENCES user_api_request (id);