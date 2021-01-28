drop table if exists file_meta;

CREATE TABLE IF NOT EXISTS file_meta (
    name VARCHAR(50) NOT NULL,
    path VARCHAR(1000) NOT NULL,
    is_directory boolean not null ,
    size BIGINT NOT NULL,
    last_modified TIMESTAMP NOT NULL,
    pinyin VARCHAR(50),
    pinyin_first VARCHAR(50)
);