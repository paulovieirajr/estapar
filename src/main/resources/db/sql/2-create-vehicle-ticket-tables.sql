-- VEHICLE

CREATE TABLE vehicle
(
    id            BINARY(16)   NOT NULL,
    license_plate VARCHAR(255) NOT NULL,
    created_at    datetime     NOT NULL,
    updated_at    datetime     NOT NULL,
    CONSTRAINT pk_vehicle PRIMARY KEY (id)
);

ALTER TABLE vehicle
    ADD CONSTRAINT uc_vehicle_license_plate UNIQUE (license_plate);

-- TICKET

CREATE TABLE ticket
(
    id           BINARY(16) NOT NULL,
    total_price  DECIMAL    NULL,
    price_rate   DECIMAL    NULL,
    valid        BIT(1)     NULL,
    entry_time   datetime   NOT NULL,
    parking_time datetime   NULL,
    exit_time    datetime   NULL,
    vehicle_id   BINARY(16) NULL,
    spot_id      INT        NULL,
    created_at   datetime   NOT NULL,
    CONSTRAINT pk_ticket PRIMARY KEY (id)
);

ALTER TABLE ticket
    ADD CONSTRAINT FK_TICKET_SPOT FOREIGN KEY (spot_id) REFERENCES spot (spot_id);

ALTER TABLE ticket
    ADD CONSTRAINT FK_TICKET_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicle (id);