package io.github.paulovieirajr.estapar.adapter.dto.webhook.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.enums.EventType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public final class WebhookEventExitDto extends WebhookEventDto {

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @JsonProperty("exit_time")
        LocalDateTime exitTime;

        public WebhookEventExitDto(
                String licensePlate,
                EventType eventType,
                LocalDateTime exitTime) {
            super(licensePlate, eventType);
            this.exitTime = exitTime;
        }

        public LocalDateTime getExitTime() {
            return exitTime;
        }
}
