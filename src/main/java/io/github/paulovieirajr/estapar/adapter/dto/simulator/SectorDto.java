package io.github.paulovieirajr.estapar.adapter.dto.simulator;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * DTO for {@link io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity}
 */
public record SectorDto(
        @JsonProperty("sector")
        String sectorCode,

        @JsonProperty("base_price")
        BigDecimal basePrice,

        @JsonProperty("max_capacity")
        Integer maxCapacity,

        @JsonProperty("open_hour")
        LocalTime openHour,

        @JsonProperty("close_hour")
        LocalTime closeHour,

        @JsonProperty("duration_limit_minutes")
        Integer durationLimitMinutes
) implements Serializable {

    public SectorEntity toEntity() {
        return new SectorEntity(
                this.sectorCode,
                this.basePrice,
                this.maxCapacity,
                this.openHour,
                this.closeHour,
                this.durationLimitMinutes
        );
    }
}