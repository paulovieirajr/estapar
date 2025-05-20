package io.github.paulovieirajr.estapar.adapter.controller.vehicle;

import io.github.paulovieirajr.estapar.adapter.dto.vehicle.LicensePlateRequestDto;
import io.github.paulovieirajr.estapar.adapter.dto.vehicle.LicensePlateResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "LicensePlateController", description = "Controller for handling license plate operations")
public interface LicensePlateSwagger {


    @Operation(method = "POST", summary = "Handle license plate status", description = "Endpoint to handle incoming license plate status.")
    @ApiResponse(responseCode = "200", description = "Successfully processed license plate status")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    ResponseEntity<LicensePlateResponseDto> processLicensePlateStatus(@RequestBody @Valid LicensePlateRequestDto licensePlateRequestDto);
}
