package io.github.paulovieirajr.estapar.adapter.controller.spot;

import io.github.paulovieirajr.estapar.adapter.dto.spot.SpotStatusRequestDto;
import io.github.paulovieirajr.estapar.adapter.dto.spot.SpotStatusResponseDto;
import io.github.paulovieirajr.estapar.service.spot.SpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/spot-status")
public class SpotStatusController implements SpotSwagger {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpotStatusController.class);

    private final SpotService spotService;

    public SpotStatusController(SpotService spotService) {
        this.spotService = spotService;
    }

    @PostMapping
    public ResponseEntity<SpotStatusResponseDto> processSpotStatus(SpotStatusRequestDto spotStatusRequestDto) {
        return spotService.getSpotStatusByLatitudeAndLongitude(
                        spotStatusRequestDto.latitude(), spotStatusRequestDto.longitude()
                )
                .map(spot -> {
                    LOGGER.info("Spot status found for latitude: {} and longitude: {}",
                            spotStatusRequestDto.latitude(), spotStatusRequestDto.longitude());
                    return ResponseEntity.ok(spot);
                })
                .orElseGet(() -> {
                    LOGGER.warn("Spot status not found for latitude: {} and longitude: {}",
                            spotStatusRequestDto.latitude(), spotStatusRequestDto.longitude());
                    return ResponseEntity.noContent().build();
                });
    }
}
