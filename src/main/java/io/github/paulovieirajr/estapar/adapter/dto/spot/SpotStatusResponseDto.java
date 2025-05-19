package io.github.paulovieirajr.estapar.adapter.dto.spot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record SpotStatusResponseDto(
        @JsonProperty("occupied")
        Boolean occupied,

        @JsonProperty("entry_time")
        LocalDateTime entryTime,

        @JsonProperty("time_parked")
        LocalDateTime timeParked
) {
}
