package io.github.paulovieirajr.estapar.adapter.dto.webhook.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.enums.EventType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public final class WebhookEventParkedDto extends WebhookEventDto {

    @NotNull
    @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90.0")
    @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90.0")
    @JsonProperty("lat")
    Double latitude;

    @NotNull
    @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180.0")
    @JsonProperty("lng")
    Double longitude;

    public WebhookEventParkedDto(
            String licensePlate,
            EventType eventType,
            Double latitude,
            Double longitude) {
        super(licensePlate, eventType);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
