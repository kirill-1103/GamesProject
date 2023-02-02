-- drop table player cascade;
-- drop table authority;
-- drop table game_chat;
-- delete from player;

CREATE TABLE IF NOT EXISTS player
(
    id    SERIAL PRIMARY KEY,
    login VARCHAR(100) UNIQUE NOT NULL ,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(64),
    sign_up_time TIMESTAMP NOT NULL,
    last_sign_in_time TIMESTAMP NOT NULL,
    rating INTEGER DEFAULT 0 NOT NULL,
    photo VARCHAR(200),
    role VARCHAR(20),
    enabled BOOLEAN DEFAULT TRUE,
    UNIQUE (login)
);

CREATE TABLE IF NOT EXISTS ttt_game
(
    id SERIAL PRIMARY KEY,
    player1_id INTEGER NOT NULL,
    player2_id INTEGER,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    winner_id INTEGER,
    size_field INT2 NOT NULL,
    base_duration INTEGER NOT NULL,
    actual_duration INTEGER,
    victory_reason_code INT2
);

CREATE TABLE IF NOT EXISTS ttt_move
(
    id    SERIAL PRIMARY KEY,
    absolute_time TIMESTAMP NOT NULL,
    game_time_millis INTEGER NOT NULL,
    x_coordinate INT2 NOT NULL ,
    y_coordinate INT2 NOT NULL,
    player_id INTEGER REFERENCES player(id),
    game_id INTEGER NOT NULL REFERENCES ttt_game(id)
);

CREATE TABLE IF NOT EXISTS game_message
(
    id SERIAL PRIMARY KEY,
    game_id INTEGER NOT NULL,
    game_code INT2 NOT NULL,
    sender_id INTEGER NOT NULL REFERENCES player(id),
    time TIMESTAMP NOT NULL,
    message TEXT NOT NULL
)
