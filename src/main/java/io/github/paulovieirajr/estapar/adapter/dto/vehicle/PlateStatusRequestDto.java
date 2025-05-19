package io.github.paulovieirajr.estapar.adapter.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record PlateStatusRequestDto(
        @NotEmpty
        @Pattern(regexp = "^[A-Z]{3}\\d{4}$|^[A-Z]{3}\\d{2}[A-Z]\\d{2}$", message = "Invalid license plate format")
        @JsonProperty("license_plate")
        String licensePlate
) {
}
