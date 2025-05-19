package io.github.paulovieirajr.estapar.adapter.controller.webhook;

import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.*;
import io.github.paulovieirajr.estapar.service.VehicleService;
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

    private final VehicleService vehicleService;

    public WebhookController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<WebhookEventResponseDto> processWebhookEvent(WebhookEventDto event) {
        switch (event.getEventType()) {
            case ENTRY -> {
                LOGGER.info("Processing entry event for license plate: {}", event.getLicensePlate());
                vehicleService.registerVehicleEntry((WebhookEventEntryDto) event);
            }
            case PARKED -> {
                LOGGER.info("Processing parked event for license plate: {}", event.getLicensePlate());
                vehicleService.registerVehicleParking((WebhookEventParkedDto) event);
            }
            case EXIT -> {
                LOGGER.info("Processing exit event for license plate: {}", event.getLicensePlate());
                vehicleService.registerVehicleExit((WebhookEventExitDto) event);
            }
            default -> LOGGER.warn("Unprocessable event type: {}", event.getEventType());
        }
        return ResponseEntity.ok(new WebhookEventResponseDto("Webhook event processed successfully"));
    }
}
