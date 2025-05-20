package io.github.paulovieirajr.estapar.adapter.controller.spot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.paulovieirajr.estapar.adapter.dto.spot.SpotStatusRequestDto;
import io.github.paulovieirajr.estapar.adapter.dto.spot.SpotStatusResponseDto;
import io.github.paulovieirajr.estapar.service.spot.SpotService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpotStatusController.class)
class SpotStatusControllerTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final LocalDateTime PARKING_TIME = LocalDateTime.of(2025, 5, 20, 10, 30, 10);
    private static final LocalDateTime ENTRY_TIME = PARKING_TIME.plusHours(1).truncatedTo(ChronoUnit.SECONDS);
    private static final double LATITUDE = -23.5505;
    private static final double LONGITUDE = -46.6333;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SpotService spotService;

    @Test
    @DisplayName("Should return spot status when is occupied")
    void shouldReturnSpotStatusWhenOccupied() throws Exception {
        SpotStatusRequestDto requestDto = new SpotStatusRequestDto(LATITUDE, LONGITUDE);
        SpotStatusResponseDto responseDto = new SpotStatusResponseDto(true, ENTRY_TIME, PARKING_TIME);

        when(spotService.getSpotStatusByLatitudeAndLongitude(anyDouble(), anyDouble()))
                .thenReturn(Optional.of(responseDto));

        mockMvc.perform(post("/spot-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.occupied").value(true))
                .andExpect(jsonPath("$.entry_time").value(ENTRY_TIME.format(FORMATTER)))
                .andExpect(jsonPath("$.time_parked").value(PARKING_TIME.format(FORMATTER)));
    }

    @Test
    @DisplayName("Should return spot status when is not occupied")
    void shouldReturnSpotStatusWhenNotOccupied() throws Exception {
        SpotStatusRequestDto requestDto = new SpotStatusRequestDto(LATITUDE, LONGITUDE);
        SpotStatusResponseDto responseDto = new SpotStatusResponseDto(false, null, null);

        when(spotService.getSpotStatusByLatitudeAndLongitude(anyDouble(), anyDouble()))
                .thenReturn(Optional.of(responseDto));

        mockMvc.perform(post("/spot-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.occupied").value(false))
                .andExpect(jsonPath("$.entry_time").doesNotExist())
                .andExpect(jsonPath("$.time_parked").doesNotExist());
    }

    @Test
    @DisplayName("Should return 204 No Content when spot status not found")
    void shouldReturnNoContentWhenSpotStatusNotFound() throws Exception {
        SpotStatusRequestDto requestDto = new SpotStatusRequestDto(LATITUDE, LONGITUDE);

        when(spotService.getSpotStatusByLatitudeAndLongitude(anyDouble(), anyDouble()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/spot-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isNoContent());
    }

    private static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
