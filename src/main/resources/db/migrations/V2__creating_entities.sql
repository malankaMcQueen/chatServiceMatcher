CREATE TABLE IF NOT EXISTS chat.chat_rooms (
    id BIGSERIAL PRIMARY KEY,
    first_user_id UUID,
    second_user_id UUID,
    time_created TIMESTAMP,
    time_last_update TIMESTAMP
);

CREATE TABLE IF NOT EXISTS chat.messages (
    id BIGSERIAL PRIMARY KEY,
    chat_room_id BIGINT REFERENCES chat.chat_rooms(id) ON DELETE CASCADE,
    sender_id UUID,
    time TIMESTAMP,
    status INTEGER,
    type INTEGER,
    content VARCHAR(255)
);