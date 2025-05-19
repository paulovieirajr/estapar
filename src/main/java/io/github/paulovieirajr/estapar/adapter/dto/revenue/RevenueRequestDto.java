package io.github.paulovieirajr.estapar.adapter.dto.revenue;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record RevenueRequestDto(
        @NotNull(message = "Date is required")
        @PastOrPresent(message = "Date cannot be in the future")
        @JsonProperty("date")
        LocalDate date,

        @NotBlank(message = "Sector is required")
        @Pattern(regexp = "^[A-Z]$", message = "Sector must be a single uppercase letter (e.g., 'A')")
        @JsonProperty("sector")
        String sectorCode
) {
}
