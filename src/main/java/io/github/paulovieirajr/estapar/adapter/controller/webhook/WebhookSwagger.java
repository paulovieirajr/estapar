package io.github.paulovieirajr.estapar.adapter.controller.webhook;

import io.github.paulovieirajr.estapar.adapter.dto.webhook.WebhookResponse;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "WebhookController", description = "Controller for handling webhook events")
public interface WebhookSwagger {


    @Operation(method = "POST", summary = "Handle webhook event", description = "Endpoint to handle incoming webhook events.")
    @ApiResponse(responseCode = "200", description = "Webhook event processed successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "409", description = "Conflict")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    ResponseEntity<WebhookResponse> handleWebhookEvent(@RequestBody WebhookEvent event);
}
