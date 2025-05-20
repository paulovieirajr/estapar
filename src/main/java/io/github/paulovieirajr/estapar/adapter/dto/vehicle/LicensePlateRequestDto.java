package io.github.paulovieirajr.estapar.adapter.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public record LicensePlateRequestDto(
        @NotEmpty
        @JsonProperty("license_plate")
        String licensePlate
) {
}
