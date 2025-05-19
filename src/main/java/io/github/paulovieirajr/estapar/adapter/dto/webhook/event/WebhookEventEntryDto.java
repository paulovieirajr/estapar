package io.github.paulovieirajr.estapar.adapter.dto.webhook.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.enums.EventType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public final class WebhookEventEntryDto extends WebhookEventDto {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("entry_time")
    LocalDateTime entryTime;

    public WebhookEventEntryDto(
            String licensePlate,
            EventType eventType,
            LocalDateTime entryTime) {
        super(licensePlate, eventType);
        this.entryTime = entryTime;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }
}
