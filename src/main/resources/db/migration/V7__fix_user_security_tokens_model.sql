
CREATE TABLE email_security_token
(
    id               INTEGER NOT NULL,
    token            VARCHAR(255),
    expire           TIMESTAMP WITHOUT TIME ZONE,
    valid            BOOLEAN,
    user_id          INTEGER,
    confirming_email VARCHAR(255),
    wd_id            INTEGER,
    CONSTRAINT pk_emailsecuritytoken PRIMARY KEY (id)
);

CREATE TABLE password_security_token
(
    id           INTEGER NOT NULL,
    token        VARCHAR(255),
    expire       TIMESTAMP WITHOUT TIME ZONE,
    valid        BOOLEAN,
    user_id      INTEGER,
    old_password VARCHAR(255),
    CONSTRAINT pk_passwordsecuritytoken PRIMARY KEY (id)
);

ALTER TABLE email_security_token
    ADD CONSTRAINT FK_EMAILSECURITYTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES usr (id);

ALTER TABLE email_security_token
    ADD CONSTRAINT FK_EMAILSECURITYTOKEN_ON_WD FOREIGN KEY (wd_id) REFERENCES usr (id);

ALTER TABLE password_security_token
    ADD CONSTRAINT FK_PASSWORDSECURITYTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES usr (id);

ALTER TABLE usr
    ADD email_token_id INTEGER;

ALTER TABLE usr
    ADD password_token_id INTEGER;

ALTER TABLE usr
    ADD CONSTRAINT FK_USR_ON_EMAIL_TOKEN FOREIGN KEY (email_token_id) REFERENCES email_security_token (id);

ALTER TABLE usr
    ADD CONSTRAINT FK_USR_ON_PASSWORD_TOKEN FOREIGN KEY (password_token_id) REFERENCES password_security_token (id);

ALTER TABLE email_security_token
    DROP CONSTRAINT fk_emailsecuritytoken_on_wd;

ALTER TABLE email_security_token
    DROP COLUMN wd_id;