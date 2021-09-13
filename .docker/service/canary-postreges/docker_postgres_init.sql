CREATE USER canary_auth WITH PASSWORD 'canary_auth' CREATEDB;
CREATE DATABASE canary_auth
    WITH
    OWNER = canary_auth
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
