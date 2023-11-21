CREATE TABLE IF NOT EXISTS player
(
    id    SERIAL PRIMARY KEY,
    login VARCHAR(100) UNIQUE NOT NULL ,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(64),
    sign_up_time TIMESTAMP NOT NULL,
    last_sign_in_time TIMESTAMP NOT NULL,
    rating INTEGER DEFAULT 0 NOT NULL,
    photo VARCHAR(300),
    role VARCHAR(20),
    enabled BOOLEAN DEFAULT TRUE,
    last_game_code INT2,
    active_time TIMESTAMP,
    UNIQUE (login)
);