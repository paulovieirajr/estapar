package io.github.paulovieirajr.estapar.adapter.controller.vehicle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.paulovieirajr.estapar.adapter.dto.vehicle.LicensePlateRequestDto;
import io.github.paulovieirajr.estapar.adapter.dto.vehicle.LicensePlateResponseDto;
import io.github.paulovieirajr.estapar.service.vehicle.VehicleService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LicensePlateController.class)
class LicensePlateControllerTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final String LICENSE_PLATE_REGISTERED = "ABC1234";
    private static final String LICENSE_PLATE_UNREGISTERED = "XYZ9876";
    private static final LocalDateTime PARKING_TIME = LocalDateTime.of(2025, 5, 20, 10, 30, 10);
    private static final LocalDateTime ENTRY_TIME = PARKING_TIME.plusHours(1).truncatedTo(ChronoUnit.SECONDS);
    private static final String PRICE_UNTIL_NOW = "10.50";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleService vehicleService;

    @Test
    @DisplayName("Should return license plate info when found")
    void shouldReturnLicensePlateInfoWhenFound() throws Exception {
        LicensePlateRequestDto requestDto = new LicensePlateRequestDto(LICENSE_PLATE_REGISTERED);
        LicensePlateResponseDto responseDto = new LicensePlateResponseDto(
                LICENSE_PLATE_REGISTERED,
                PRICE_UNTIL_NOW,
                ENTRY_TIME,
                PARKING_TIME
        );

        when(vehicleService.searchLicensePlate(any())).thenReturn(Optional.of(responseDto));

        mockMvc.perform(post("/plate-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.license_plate").value(LICENSE_PLATE_REGISTERED))
                .andExpect(jsonPath("$.price_until_now").value(PRICE_UNTIL_NOW))
                .andExpect(jsonPath("$.entry_time").value(ENTRY_TIME.format(FORMATTER)))
                .andExpect(jsonPath("$.time_parked").value(PARKING_TIME.format(FORMATTER)));
    }

    @Test
    @DisplayName("Should return no content when license plate not found")
    void shouldReturnNoContentWhenLicensePlateNotFound() throws Exception {
        LicensePlateRequestDto requestDto = new LicensePlateRequestDto(LICENSE_PLATE_UNREGISTERED);

        when(vehicleService.searchLicensePlate(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/plate-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isNoContent());
    }

    private static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
