package io.github.paulovieirajr.estapar.adapter.controller.webhook;

import io.github.paulovieirajr.estapar.adapter.dto.webhook.WebhookResponse;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookController implements WebhookSwagger{

    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping
    public ResponseEntity<WebhookResponse> processWebhookEvent(WebhookEvent event) {
        switch (event.getEventType()) {
            case ENTRY -> LOGGER.info("Processing entry event for license plate: {}", event.getLicensePlate());
            case PARKED -> LOGGER.info("Processing parked event for license plate: {}", event.getLicensePlate());
            case EXIT -> LOGGER.info("Processing exit event for license plate: {}", event.getLicensePlate());
            default -> LOGGER.warn("Unknown event type: {}", event.getEventType());
        }
        return ResponseEntity.ok(new WebhookResponse("Webhook event processed successfully"));
    }
}
