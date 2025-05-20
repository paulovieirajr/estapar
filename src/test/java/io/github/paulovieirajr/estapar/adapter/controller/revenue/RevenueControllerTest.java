package io.github.paulovieirajr.estapar.adapter.controller.revenue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.paulovieirajr.estapar.adapter.dto.revenue.RevenueRequestDto;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.RevenueEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;
import io.github.paulovieirajr.estapar.service.revenue.RevenueService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RevenueController.class)
class RevenueControllerTest {

    private static final LocalDate REQUEST_DATE = LocalDate.of(2025, 5, 20);
    private static final BigDecimal AMOUNT = new BigDecimal("240.42");
    private static final String CURRENCY_CODE = "BRL";
    private static final String SECTOR_CODE = "A";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RevenueService revenueService;

    @Test
    @DisplayName("Should return revenue data when found")
    void shouldReturnRevenueDataWhenFound() throws Exception {
        RevenueRequestDto requestDto = new RevenueRequestDto(REQUEST_DATE, SECTOR_CODE);

        SectorEntity mockSector = mock(SectorEntity.class);
        RevenueEntity revenueEntity = new RevenueEntity(REQUEST_DATE, AMOUNT, mockSector);

        when(revenueService.fetchRevenueBySector(any())).thenReturn(Optional.of(revenueEntity));

        mockMvc.perform(post("/revenue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(AMOUNT))
                .andExpect(jsonPath("$.currency").value(CURRENCY_CODE))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Should return 404 when revenue not found")
    void shouldReturnNotFoundWhenRevenueNotFound() throws Exception {
        RevenueRequestDto requestDto = new RevenueRequestDto(REQUEST_DATE, SECTOR_CODE);

        when(revenueService.fetchRevenueBySector(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/revenue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isNotFound());
    }

    private static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(obj);
    }
}


