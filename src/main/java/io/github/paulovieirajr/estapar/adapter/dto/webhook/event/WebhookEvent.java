package io.github.paulovieirajr.estapar.adapter.dto.webhook.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.enums.EventType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public sealed class WebhookEvent permits WebhookEventEntryDto, WebhookEventExitDto, WebhookEventParkedDto {

    @NotEmpty
    @Pattern(regexp = "^[A-Z]{3}\\d{4}$|^[A-Z]{3}\\d{2}[A-Z]\\d{2}$", message = "Invalid license plate format")
    @JsonProperty("license_plate")
    String licensePlate;

    @NotEmpty
    @JsonProperty("event_type")
    EventType eventType;

    public String getLicensePlate() {
        return licensePlate;
    }

    public EventType getEventType() {
        return eventType;
    }
}
