-- drop table player cascade;
-- drop table authority;

CREATE TABLE IF NOT EXISTS player
(
    id    SERIAL PRIMARY KEY,
    login VARCHAR(100) NOT NULL ,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(64),
    sign_up_time TIMESTAMP NOT NULL,
    last_sign_in_time TIMESTAMP NOT NULL,
    rating INTEGER DEFAULT 0 NOT NULL,
    photo VARCHAR(200),
    role VARCHAR(20),
    enabled BOOLEAN DEFAULT TRUE,
    UNIQUE (login)
);

