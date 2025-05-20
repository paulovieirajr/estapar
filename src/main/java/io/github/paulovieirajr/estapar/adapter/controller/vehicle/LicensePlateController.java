package io.github.paulovieirajr.estapar.adapter.controller.vehicle;

import io.github.paulovieirajr.estapar.adapter.dto.vehicle.LicensePlateRequestDto;
import io.github.paulovieirajr.estapar.adapter.dto.vehicle.LicensePlateResponseDto;
import io.github.paulovieirajr.estapar.service.vehicle.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/plate-status")
public class LicensePlateController implements LicensePlateSwagger {

    private static final Logger LOGGER = LoggerFactory.getLogger(LicensePlateController.class);

    private final VehicleService vehicleService;

    public LicensePlateController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<LicensePlateResponseDto> processLicensePlateStatus(LicensePlateRequestDto licensePlateRequestDto) {
        LOGGER.info("Processing status for license plate: {}", licensePlateRequestDto.licensePlate());

        return vehicleService.searchLicensePlate(licensePlateRequestDto)
                .map(licensePlate -> {
                    LOGGER.info("License plate {} found in the system", licensePlateRequestDto.licensePlate());
                    return ResponseEntity.ok(licensePlate);
                })
                .orElseGet(() -> {
                    LOGGER.warn("License plate {} not found in the system", licensePlateRequestDto.licensePlate());
                    return ResponseEntity.noContent().build();
                });
    }
}
