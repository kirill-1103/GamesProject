DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'friend_response') THEN
        CREATE TYPE friend_response AS ENUM ('accept', 'reject', 'pending', 'revoked');
    END IF;
END $$;

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

CREATE TABLE IF NOT EXISTS friends
(
    id1 INTEGER,
    id2 INTEGER,
    friend_time TIMESTAMP NOT NULL,
    PRIMARY KEY (id1,id2),
    FOREIGN KEY (id1) REFERENCES player (id) ON DELETE NO ACTION,
    FOREIGN KEY (id2) REFERENCES player (id) ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS friend_request
(
    id SERIAL PRIMARY KEY,
    request_date TIMESTAMP,
    response_date TIMESTAMP,
    sender_id INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    response FRIEND_RESPONSE NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES player (id) ON DELETE NO ACTION,
    FOREIGN KEY (sender_id) REFERENCES player (id) ON DELETE NO ACTION
);