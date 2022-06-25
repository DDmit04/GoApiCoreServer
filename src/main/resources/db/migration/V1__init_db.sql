
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE app_service_bill
(
    id               INTEGER                     NOT NULL,
    money_left       DECIMAL(19, 2)              NOT NULL,
    user_id          INTEGER,
    bill_type        VARCHAR(255)                NOT NULL,
    last_payout_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_app_service_bill PRIMARY KEY (id)
);

CREATE TABLE app_service_object
(
    id            INTEGER                     NOT NULL,
    name          VARCHAR(255)                NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id       INTEGER                     NOT NULL,
    bill_id       INTEGER                     NOT NULL,
    tariff_id     INTEGER                     NOT NULL,
    disabled      INTEGER                     NOT NULL,
    disabled_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_appserviceobject PRIMARY KEY (id)
);

CREATE TABLE app_service_payment
(
    id                    INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    date                  TIMESTAMP WITHOUT TIME ZONE              NOT NULL,
    sum                   DECIMAL(19, 2)                           NOT NULL,
    description           VARCHAR(255)                             NOT NULL,
    payment_status        VARCHAR(255)                             NOT NULL,
    payment_status_reason VARCHAR(255)                             NOT NULL,
    from_user_bill_id     INTEGER                                  NOT NULL,
    to_service_bill_id    INTEGER                                  NOT NULL,
    CONSTRAINT pk_appservicepayment PRIMARY KEY (id)
);

CREATE TABLE app_service_payout
(
    id                    INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    date                  TIMESTAMP WITHOUT TIME ZONE              NOT NULL,
    sum                   DECIMAL(19, 2)                           NOT NULL,
    description           VARCHAR(255)                             NOT NULL,
    payment_status        VARCHAR(255)                             NOT NULL,
    payment_status_reason VARCHAR(255)                             NOT NULL,
    app_service_bill_id   INTEGER                                  NOT NULL,
    CONSTRAINT pk_appservicepayout PRIMARY KEY (id)
);

CREATE TABLE database
(
    id                          INTEGER      NOT NULL,
    database_password           BYTEA        NOT NULL,
    database_type               VARCHAR(255) NOT NULL,
    accept_external_connections BOOLEAN      NOT NULL,
    CONSTRAINT pk_database PRIMARY KEY (id)
);

CREATE TABLE database_tariff
(
    id             INTEGER NOT NULL,
    max_size_bytes BIGINT  NOT NULL,
    CONSTRAINT pk_databasetariff PRIMARY KEY (id)
);

CREATE TABLE email_security_token
(
    id               INTEGER                     NOT NULL,
    token            VARCHAR(255)                NOT NULL,
    expire           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    valid            BOOLEAN                     NOT NULL,
    user_id          INTEGER                     NOT NULL,
    confirming_email VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_emailsecuritytoken PRIMARY KEY (id)
);

CREATE TABLE password_security_token
(
    id           INTEGER                     NOT NULL,
    token        VARCHAR(255)                NOT NULL,
    expire       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    valid        BOOLEAN                     NOT NULL,
    user_id      INTEGER                     NOT NULL,
    old_password VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_passwordsecuritytoken PRIMARY KEY (id)
);

CREATE TABLE tariff
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    tariff_name  VARCHAR(255)                             NOT NULL,
    cost_per_day DECIMAL DEFAULT 0                        NOT NULL,
    CONSTRAINT pk_tariff PRIMARY KEY (id)
);

CREATE TABLE user_api
(
    id           INTEGER      NOT NULL,
    api_key      VARCHAR(255) NOT NULL,
    is_protected BOOLEAN      NOT NULL,
    database_id  INTEGER      NOT NULL,
    CONSTRAINT pk_userapi PRIMARY KEY (id)
);

CREATE TABLE user_api_request
(
    id               INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_api_id      INTEGER                                  NOT NULL,
    request_name     VARCHAR(255)                             NOT NULL,
    request_template VARCHAR(255)                             NOT NULL,
    http_method      VARCHAR(255)                             NOT NULL,
    CONSTRAINT pk_userapirequest PRIMARY KEY (id)
);

CREATE TABLE user_api_request_argument
(
    id                    INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    arg_name              VARCHAR(255)                             NOT NULL,
    request_argument_type VARCHAR(255)                             NOT NULL,
    api_request_id        INTEGER                                  NOT NULL,
    CONSTRAINT pk_userapirequestargument PRIMARY KEY (id)
);

CREATE TABLE user_api_tariff
(
    id             INTEGER NOT NULL,
    requests_count INTEGER NOT NULL,
    CONSTRAINT pk_userapitariff PRIMARY KEY (id)
);

CREATE TABLE user_bill
(
    id         INTEGER        NOT NULL,
    money_left DECIMAL(19, 2) NOT NULL,
    user_id    INTEGER,
    CONSTRAINT pk_user_bill PRIMARY KEY (id)
);

CREATE TABLE user_payment
(
    id                    INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    date                  TIMESTAMP WITHOUT TIME ZONE              NOT NULL,
    sum                   DECIMAL(19, 2)                           NOT NULL,
    description           VARCHAR(255)                             NOT NULL,
    payment_status        VARCHAR(255)                             NOT NULL,
    payment_status_reason VARCHAR(255)                             NOT NULL,
    user_bill_id          INTEGER                                  NOT NULL,
    user_id               INTEGER,
    CONSTRAINT pk_userpayment PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    user_id INTEGER NOT NULL,
    roles   VARCHAR(255)
);

CREATE TABLE usr
(
    id                 INTEGER      NOT NULL,
    username           VARCHAR(255) NOT NULL,
    user_password      VARCHAR(255) NOT NULL,
    email              VARCHAR(255) NOT NULL,
    is_email_confirmed BOOLEAN      NOT NULL,
    jwt_refresh_token  VARCHAR(255) NOT NULL,
    bill_id            INTEGER      NOT NULL,
    email_token_id     INTEGER,
    password_token_id  INTEGER,
    CONSTRAINT pk_usr PRIMARY KEY (id)
);

ALTER TABLE usr
    ADD CONSTRAINT uc_usr_email UNIQUE (email);

ALTER TABLE usr
    ADD CONSTRAINT uc_usr_username UNIQUE (username);

ALTER TABLE app_service_object
    ADD CONSTRAINT FK_APPSERVICEOBJECT_ON_BILL FOREIGN KEY (bill_id) REFERENCES app_service_bill (id);

ALTER TABLE app_service_object
    ADD CONSTRAINT FK_APPSERVICEOBJECT_ON_TARIFF FOREIGN KEY (tariff_id) REFERENCES tariff (id);

ALTER TABLE app_service_object
    ADD CONSTRAINT FK_APPSERVICEOBJECT_ON_USER FOREIGN KEY (user_id) REFERENCES usr (id);

ALTER TABLE app_service_payment
    ADD CONSTRAINT FK_APPSERVICEPAYMENT_ON_FROM_USER_BILL FOREIGN KEY (from_user_bill_id) REFERENCES user_bill (id);

ALTER TABLE app_service_payment
    ADD CONSTRAINT FK_APPSERVICEPAYMENT_ON_TO_SERVICE_BILL FOREIGN KEY (to_service_bill_id) REFERENCES app_service_bill (id);

ALTER TABLE app_service_payout
    ADD CONSTRAINT FK_APPSERVICEPAYOUT_ON_APP_SERVICE_BILL FOREIGN KEY (app_service_bill_id) REFERENCES app_service_bill (id);

ALTER TABLE app_service_bill
    ADD CONSTRAINT FK_APP_SERVICE_BILL_ON_USER FOREIGN KEY (user_id) REFERENCES usr (id);

ALTER TABLE database_tariff
    ADD CONSTRAINT FK_DATABASETARIFF_ON_ID FOREIGN KEY (id) REFERENCES tariff (id);

ALTER TABLE database
    ADD CONSTRAINT FK_DATABASE_ON_ID FOREIGN KEY (id) REFERENCES app_service_object (id);

ALTER TABLE email_security_token
    ADD CONSTRAINT FK_EMAILSECURITYTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES usr (id);

ALTER TABLE password_security_token
    ADD CONSTRAINT FK_PASSWORDSECURITYTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES usr (id);

ALTER TABLE user_api_request_argument
    ADD CONSTRAINT FK_USERAPIREQUESTARGUMENT_ON_API_REQUEST FOREIGN KEY (api_request_id) REFERENCES user_api_request (id);

ALTER TABLE user_api_request
    ADD CONSTRAINT FK_USERAPIREQUEST_ON_USER_API FOREIGN KEY (user_api_id) REFERENCES user_api (id);

ALTER TABLE user_api_tariff
    ADD CONSTRAINT FK_USERAPITARIFF_ON_ID FOREIGN KEY (id) REFERENCES tariff (id);

ALTER TABLE user_api
    ADD CONSTRAINT FK_USERAPI_ON_DATABASE FOREIGN KEY (database_id) REFERENCES database (id);

ALTER TABLE user_api
    ADD CONSTRAINT FK_USERAPI_ON_ID FOREIGN KEY (id) REFERENCES app_service_object (id);

ALTER TABLE user_payment
    ADD CONSTRAINT FK_USERPAYMENT_ON_USER FOREIGN KEY (user_id) REFERENCES user_bill (id);

ALTER TABLE user_payment
    ADD CONSTRAINT FK_USERPAYMENT_ON_USER_BILL FOREIGN KEY (user_bill_id) REFERENCES user_bill (id);

ALTER TABLE user_bill
    ADD CONSTRAINT FK_USER_BILL_ON_USER FOREIGN KEY (user_id) REFERENCES usr (id);

ALTER TABLE usr
    ADD CONSTRAINT FK_USR_ON_BILL FOREIGN KEY (bill_id) REFERENCES user_bill (id);

ALTER TABLE usr
    ADD CONSTRAINT FK_USR_ON_EMAIL_TOKEN FOREIGN KEY (email_token_id) REFERENCES email_security_token (id);

ALTER TABLE usr
    ADD CONSTRAINT FK_USR_ON_PASSWORD_TOKEN FOREIGN KEY (password_token_id) REFERENCES password_security_token (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_user FOREIGN KEY (user_id) REFERENCES usr (id);