package io.github.paulovieirajr.estapar.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SpotEntity;

import java.io.Serializable;

/**
 * DTO for {@link io.github.paulovieirajr.estapar.adapter.persistence.entity.SpotEntity}
 */
public record SpotDto(
        @JsonProperty("id")
        Integer id,

        @JsonProperty("sector")
        String sectorCode,

        @JsonProperty("lat")
        Double latitude,

        @JsonProperty("lng")
        Double longitude,

        @JsonProperty("occupied")
        boolean occupied
) implements Serializable {

    public SpotEntity toEntity() {
        return new SpotEntity(
                this.id,
                this.latitude,
                this.longitude,
                this.occupied
        );
    }
}