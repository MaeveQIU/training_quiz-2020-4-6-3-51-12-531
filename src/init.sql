DROP DATABASE parking_sys;
CREATE DATABASE parking_sys;
USE parking_sys;
CREATE TABLE IF NOT EXISTS all_parking_lots (
    name VARCHAR(8),
    capacity SMALLINT
);

SET SQL_SAFE_UPDATES = 0;