CREATE TABLE IF NOT EXISTS t_song (
    song_id SERIAL PRIMARY KEY,
    guid UUID DEFAULT uuid(),
    title VARCHAR(64) NOT NULL,
    source_id VARCHAR(64),
    source_system VARCHAR(64),
    created_by VARCHAR(64) DEFAULT 'SYSTEM',
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_by VARCHAR(64) DEFAULT 'SYSTEM',
    modified_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_author (
    auth_id SERIAL PRIMARY KEY,
    auth_song_id NUMBER,
    author VARCHAR(64) NOT NULL,
    FOREIGN KEY (auth_song_id) REFERENCES t_song(song_id)
);

CREATE TABLE IF NOT EXISTS t_theme (
    them_id SERIAL PRIMARY KEY,
    them_song_id NUMBER,
    theme VARCHAR(64) NOT NULL,
    FOREIGN KEY (them_song_id) REFERENCES t_song(song_id)
);
