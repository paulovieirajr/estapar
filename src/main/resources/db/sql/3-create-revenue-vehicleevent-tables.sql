-- REVENUE

CREATE TABLE revenue
(
    id            BINARY(16)   NOT NULL,
    date          date         NOT NULL,
    amount        DECIMAL      NOT NULL,
    currency_code VARCHAR(255) NOT NULL,
    sector_code   VARCHAR(5)   NOT NULL,
    created_at    datetime     NOT NULL,
    updated_at    datetime     NOT NULL,
    CONSTRAINT pk_revenue PRIMARY KEY (id)
);

ALTER TABLE revenue
    ADD CONSTRAINT FK_REVENUE_SECTOR FOREIGN KEY (sector_code) REFERENCES sector (sector_code);

-- VEHICLE EVENT

CREATE TABLE vehicle_event
(
    id         BINARY(16)   NOT NULL,
    event_date datetime     NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    vehicle_id BINARY(16)   NOT NULL,
    spot_id    INT          NULL,
    created_at datetime     NOT NULL,
    CONSTRAINT pk_vehicle_event PRIMARY KEY (id)
);

ALTER TABLE vehicle_event
    ADD CONSTRAINT FK_VEHICLE_EVENT_SPOT FOREIGN KEY (spot_id) REFERENCES spot (spot_id);

ALTER TABLE vehicle_event
    ADD CONSTRAINT FK_VEHICLE_EVENT_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicle (id);