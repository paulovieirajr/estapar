package io.github.paulovieirajr.estapar.service.spot;

import io.github.paulovieirajr.estapar.adapter.dto.spot.SpotStatusResponseDto;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.SpotRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpotService {

    private static final Logger logger = LoggerFactory.getLogger(SpotService.class.getName());

    private final TicketRepository ticketRepository;
    private final SpotRepository spotRepository;

    public SpotService(TicketRepository ticketRepository, SpotRepository spotRepository) {
        this.ticketRepository = ticketRepository;
        this.spotRepository = spotRepository;
    }

    public Optional<SpotStatusResponseDto> getSpotStatusByLatitudeAndLongitude(Double latitude, Double longitude) {
        logger.info("Recovering spot status by latitude and longitude: {}, {}", latitude, longitude);

        return spotRepository.findByLatitudeAndLongitude(latitude, longitude)
                .map(spot -> {
                    if (spot.isOccupied()) {
                        logger.info("Spot is occupied");
                        return ticketRepository.findBySpot(spot)
                                .map(ticket ->
                                        new SpotStatusResponseDto(true, ticket.getEntryTime(), ticket.getParkingTime()));
                    } else {
                        logger.info("Spot is free");
                        return Optional.of(new SpotStatusResponseDto(false, null, null));
                    }
                })
                .orElseGet(() -> {
                    logger.warn("Spot not found with latitude: {} and longitude: {}", latitude, longitude);
                    return Optional.empty();
                });
    }
}
