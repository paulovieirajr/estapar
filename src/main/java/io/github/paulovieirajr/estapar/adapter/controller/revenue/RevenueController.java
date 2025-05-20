package io.github.paulovieirajr.estapar.adapter.controller.revenue;

import io.github.paulovieirajr.estapar.adapter.dto.revenue.RevenueRequestDto;
import io.github.paulovieirajr.estapar.adapter.dto.revenue.RevenueResponseDto;
import io.github.paulovieirajr.estapar.service.revenue.RevenueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@RestController
@Validated
@RequestMapping("/revenue")
public class RevenueController implements RevenueSwagger {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevenueController.class);

    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @PostMapping
    public ResponseEntity<RevenueResponseDto> processRevenue(RevenueRequestDto revenueRequestDto) {
        LOGGER.info("Processing Revenue for date: {}", revenueRequestDto.date());

        return revenueService.fetchRevenueBySector(revenueRequestDto)
                .map(revenue -> {
                    LOGGER.info("Revenue found: {}", revenue);
                    return ResponseEntity.ok(new RevenueResponseDto(
                            revenue.getAmount(),
                            revenue.getCurrencyCode(),
                            ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime()
                    ));
                })
                .orElseGet(() -> {
                    LOGGER.info("No revenue found for date: {}", revenueRequestDto.date());
                    return ResponseEntity.notFound().build();
                });
    }
}
