package io.github.paulovieirajr.estapar.adapter.dto.revenue;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RevenueResponseDto(
        @JsonProperty("amount")
        BigDecimal amount,

        @JsonProperty("currency")
        String currencyCode,

        @JsonProperty("timestamp")
        LocalDateTime timestamp
) {
}
