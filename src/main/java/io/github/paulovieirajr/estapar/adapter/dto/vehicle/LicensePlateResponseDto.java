package io.github.paulovieirajr.estapar.adapter.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record LicensePlateResponseDto(
        @JsonProperty("license_plate")
        String licensePlate,

        @JsonProperty("price_until_now")
        String priceUntilNow,

        @JsonProperty("entry_time")
        LocalDateTime entryTime,

        @JsonProperty("time_parked")
        LocalDateTime timeParked
) {
}
