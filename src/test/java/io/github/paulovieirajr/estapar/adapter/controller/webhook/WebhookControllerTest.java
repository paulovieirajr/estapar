package io.github.paulovieirajr.estapar.adapter.controller.webhook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.enums.EventType;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEventEntryDto;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEventExitDto;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEventParkedDto;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEventResponseDto;
import io.github.paulovieirajr.estapar.service.vehicle.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebhookController.class)
class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleService vehicleService;

    @Test
    void shouldProcessEntryEvent() throws Exception {
        WebhookEventEntryDto entryDto = new WebhookEventEntryDto("ABC1234", EventType.ENTRY, LocalDateTime.now());

        WebhookEventResponseDto response = new WebhookEventResponseDto("ENTRY processed");
        when(vehicleService.registerVehicleEntry(any())).thenReturn(response);

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(entryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ENTRY processed"));
    }

    @Test
    void shouldProcessParkedEvent() throws Exception {
        WebhookEventParkedDto parkedDto = new WebhookEventParkedDto(
                "XYZ9876", EventType.PARKED,
                -23.4325, -46.5678);

        WebhookEventResponseDto response = new WebhookEventResponseDto("PARKED processed");
        when(vehicleService.registerVehicleParking(any())).thenReturn(response);

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(parkedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PARKED processed"));
    }

    @Test
    void shouldProcessExitEvent() throws Exception {
        WebhookEventExitDto exitDto = new WebhookEventExitDto("JKL5678", EventType.EXIT, LocalDateTime.now());

        WebhookEventResponseDto response = new WebhookEventResponseDto("EXIT processed");
        when(vehicleService.registerVehicleExit(any())).thenReturn(response);

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(exitDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("EXIT processed"));
    }

    private static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModules(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }
}
