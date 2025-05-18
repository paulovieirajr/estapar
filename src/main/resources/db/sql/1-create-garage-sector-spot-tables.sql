-- GARAGE

CREATE TABLE IF NOT EXISTS garage
(
    garage_id  BINARY(16) NOT NULL,
    created_at datetime   NOT NULL,
    CONSTRAINT pk_garage PRIMARY KEY (garage_id)
);

-- SECTOR

CREATE TABLE IF NOT EXISTS sector
(
    sector_code            VARCHAR(5) NOT NULL,
    base_price             DECIMAL    NOT NULL,
    max_capacity           INT        NOT NULL,
    open_hour              time       NOT NULL,
    close_hour             time       NOT NULL,
    duration_limit_minutes INT        NOT NULL,
    garage_id              BINARY(16) NOT NULL,
    created_at             datetime   NOT NULL,
    updated_at             datetime   NOT NULL,
    CONSTRAINT pk_sector PRIMARY KEY (sector_code)
);

ALTER TABLE sector
    ADD CONSTRAINT fk_sector_garage FOREIGN KEY (garage_id) REFERENCES garage (garage_id);

-- SPOT

CREATE TABLE IF NOT EXISTS spot
(
    spot_id     INT        NOT NULL,
    latitude    DOUBLE     NOT NULL,
    longitude   DOUBLE     NOT NULL,
    sector_code VARCHAR(5) NOT NULL,
    occupied    BIT(1)     NOT NULL,
    created_at  datetime   NOT NULL,
    updated_at  datetime   NOT NULL,
    CONSTRAINT pk_spot PRIMARY KEY (spot_id)
);

ALTER TABLE spot
    ADD CONSTRAINT uc_lattd_longt UNIQUE (latitude, longitude);

ALTER TABLE spot
    ADD CONSTRAINT fk_spot_sector FOREIGN KEY (sector_code) REFERENCES sector (sector_code);