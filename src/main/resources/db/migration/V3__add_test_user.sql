
insert into usr(id, email, user_password, jwt_refresh_token, username) VALUES
-- password - 123
(1, 'testEmail@gmail.com', '123', '03c55986-b8a6-11ec-b909-0242ac120002', 'user');

insert into user_role(user_id, roles) VALUES (1, 'USER');

create extension if not exists pgcrypto;

update usr set user_password = crypt(user_password, gen_salt('bf', 8));

insert into user_bill(id, money_left, user_id) values (1, 1000, 1);