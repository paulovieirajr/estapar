package io.github.paulovieirajr.estapar.adapter.dto.webhook.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.enums.EventType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "event_type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WebhookEventEntryDto.class, name = "ENTRY"),
        @JsonSubTypes.Type(value = WebhookEventExitDto.class, name = "EXIT"),
        @JsonSubTypes.Type(value = WebhookEventParkedDto.class, name = "PARKED")
})
public sealed class WebhookEventDto permits WebhookEventEntryDto, WebhookEventExitDto, WebhookEventParkedDto {

    @NotEmpty
    @Pattern(regexp = "^[A-Z]{3}\\d{4}$|^[A-Z]{3}\\d{2}[A-Z]\\d{2}$", message = "Invalid license plate format")
    @JsonProperty("license_plate")
    String licensePlate;

    @NotEmpty
    @JsonProperty("event_type")
    EventType eventType;

    public WebhookEventDto(String licensePlate, EventType eventType) {
        this.licensePlate = licensePlate;
        this.eventType = eventType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public EventType getEventType() {
        return eventType;
    }
}
