package io.github.paulovieirajr.estapar.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record EstaparDataSimulatorDto(
        @JsonProperty("garage")
        List<SectorDto> sectors,

        @JsonProperty("spots")
        List<SpotDto> spots
) {
}
