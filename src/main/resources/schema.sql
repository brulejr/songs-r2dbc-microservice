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

CREATE TABLE IF NOT EXISTS t_song_value (
    sval_id SERIAL PRIMARY KEY,
    sval_song_id NUMBER,
    song_value VARCHAR(64) NOT NULL,
    song_value_type VARCHAR(23) NOT NULL,
    FOREIGN KEY (sval_song_id) REFERENCES t_song(song_id)
);
