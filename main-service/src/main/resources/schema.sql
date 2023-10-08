-- drop table player cascade;
-- drop table authority;
-- drop table game_chat;
-- delete from player;
-- ALTER TABLE player ADD COLUMN last_game_code INT2;

-- drop table ttt_move;
-- drop table ttt_game;
-- drop table tetris_game;

-- insert into ttt_game (id,player1_id,start_time,size_field,player1_time,player2_time,base_player_time)
-- values (1,66,TIMESTAMP '2011-05-16 15:36:38', 3,3,3,3);

-- update player set last_game_code = null where login='kirill';
-- update ttt_game set end_time = TIMESTAMP '2011-05-16 15:36:38';


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
    last_game_code INT2,
    active_time TIMESTAMP,
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
    player1_time INTEGER NOT NULL,
    player2_time INTEGER NOT NULL,
    base_player_time INTEGER NOT NULL,
    actual_duration INTEGER,
    victory_reason_code INT2,
    complexity INT2,
    queue INT2
);

CREATE TABLE IF NOT EXISTS ttt_move
(
    id    SERIAL PRIMARY KEY,
    absolute_time TIMESTAMP NOT NULL,
    game_time_millis INTEGER NOT NULL,
    x_coordinate INT2 NOT NULL ,
    y_coordinate INT2 NOT NULL,
    player_id INTEGER,
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
);

-- drop table message;

CREATE TABLE IF NOT EXISTS message
(
    id SERIAL PRIMARY KEY,
    sender_id INTEGER NOT NULL REFERENCES player(id),
    recipient_id INTEGER NOT NULL REFERENCES player(id),
    sending_time TIMESTAMP NOT NULL,
    reading_time TIMESTAMP,
    message_text TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS tetris_game
(
    id SERIAL PRIMARY KEY,
    player1_id INTEGER NOT NULL,
    player2_id INTEGER,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    winner_id INTEGER,
    duration INTEGER,
    field_1 TEXT,
    field_2 TEXT,
    player1_points INTEGER NOT NULL,
    player2_points INTEGER,
    player1_time INTEGER NOT NULL,
    player2_time INTEGER
);