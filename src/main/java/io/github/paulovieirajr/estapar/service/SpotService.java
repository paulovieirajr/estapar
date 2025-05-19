package io.github.paulovieirajr.estapar.service;

import io.github.paulovieirajr.estapar.adapter.dto.spot.SpotStatusResponseDto;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SpotEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.TicketEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.SpotRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.TicketRepository;
import io.github.paulovieirajr.estapar.service.exception.spot.SpotNotFoundException;
import io.github.paulovieirajr.estapar.service.exception.ticket.TicketNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SpotService {

    private static final Logger logger = LoggerFactory.getLogger(SpotService.class.getName());

    private final TicketRepository ticketRepository;
    private final SpotRepository spotRepository;

    public SpotService(TicketRepository ticketRepository, SpotRepository spotRepository) {
        this.ticketRepository = ticketRepository;
        this.spotRepository = spotRepository;
    }

    public SpotStatusResponseDto getSpotStatusByLatitudeAndLongitude(Double latitude, Double longitude) {
        logger.info("Recovering spot status by latitude and longitude: {}, {}", latitude, longitude);

        SpotEntity spot = spotRepository.findByLatitudeAndLongitude(latitude, longitude)
                .orElseThrow(
                        () -> new SpotNotFoundException("Spot not found with latitude: " + latitude + " and longitude: " + longitude)
                );

        if (spot.isOccupied()) {
            logger.info("Spot is occupied");
            TicketEntity ticket = ticketRepository.findBySpot(spot)
                    .orElseThrow(() -> new TicketNotFoundException("Ticket not found for spot with id: " + spot.getId())
                    );
            return new SpotStatusResponseDto(spot.isOccupied(), ticket.getEntryTime(), ticket.getParkingTime());
        } else {
            logger.info("Spot is free");
            return new SpotStatusResponseDto(spot.isOccupied(), null, null);
        }
    }
}
