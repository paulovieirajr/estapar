package io.github.paulovieirajr.estapar.adapter.controller.revenue;

import io.github.paulovieirajr.estapar.adapter.dto.revenue.RevenueRequestDto;
import io.github.paulovieirajr.estapar.adapter.dto.revenue.RevenueResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "RevenueController", description = "Controller for handling revenue operations")
public interface RevenueSwagger {

    @Operation(method = "POST", summary = "Handle spot status", description = "Endpoint to handle incoming spot status.")
    @ApiResponse(responseCode = "200", description = "Successfully processed spot status")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    ResponseEntity<RevenueResponseDto> processRevenue(@RequestBody @Valid RevenueRequestDto revenueRequestDto);
}
