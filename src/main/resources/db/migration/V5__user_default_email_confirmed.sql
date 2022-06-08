update usr set is_email_confirmed = false;

alter table usr alter column is_email_confirmed set default false;