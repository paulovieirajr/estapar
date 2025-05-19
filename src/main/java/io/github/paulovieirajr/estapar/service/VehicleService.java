package io.github.paulovieirajr.estapar.service;

import io.github.paulovieirajr.estapar.adapter.dto.vehicle.PlateStatusRequestDto;
import io.github.paulovieirajr.estapar.adapter.dto.vehicle.PlateStatusResponseDto;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEventEntryDto;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEventExitDto;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEventParkedDto;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEventResponseDto;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.*;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.SpotRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.TicketRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.VehicleEventRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.VehicleRepository;
import io.github.paulovieirajr.estapar.domain.model.Ticket;
import io.github.paulovieirajr.estapar.service.exception.sector.SectorAlreadyFullException;
import io.github.paulovieirajr.estapar.service.exception.spot.SpotAlreadyOccupiedException;
import io.github.paulovieirajr.estapar.service.exception.spot.SpotNotFoundException;
import io.github.paulovieirajr.estapar.service.exception.ticket.TicketNotFoundException;
import io.github.paulovieirajr.estapar.service.exception.vehicle.VechicleNotFoundException;
import io.github.paulovieirajr.estapar.service.exception.vehicle.VehicleAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class VehicleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleService.class);

    private final SectorService sectorService;
    private final TicketRepository ticketRepository;
    private final VehicleEventRepository vehicleEventRepository;
    private final SpotRepository spotRepository;
    private final VehicleRepository vehicleRepository;
    private final RevenueService revenueService;

    public VehicleService(SectorService sectorService, TicketRepository ticketRepository,
                          VehicleEventRepository vehicleEventRepository, SpotRepository spotRepository,
                          VehicleRepository vehicleRepository, RevenueService revenueService) {
        this.sectorService = sectorService;
        this.ticketRepository = ticketRepository;
        this.vehicleEventRepository = vehicleEventRepository;
        this.spotRepository = spotRepository;
        this.vehicleRepository = vehicleRepository;
        this.revenueService = revenueService;
    }

    public WebhookEventResponseDto registerVehicleEntry(WebhookEventEntryDto eventEntryDto) {
        LOGGER.info("Registering vehicle entry: {}", eventEntryDto);

        if (sectorService.areAllSectorsTotallyOccupied()) {
            LOGGER.warn("Sectors are totally occupied. Cannot register vehicle entry.");
            throw new SectorAlreadyFullException("Cannot register vehicle entry. All sectors are full.");
        }

        vehicleRepository.findByLicensePlate(eventEntryDto.getLicensePlate())
                .ifPresent(vehicle -> {
                    throw new VehicleAlreadyExistsException("Vehicle already registered with license plate: " + eventEntryDto.getLicensePlate());
                });

        VehicleEntity savedVehicle = vehicleRepository.save(new VehicleEntity(eventEntryDto.getLicensePlate()));

        VehicleEventEntity vehicleEvent = new VehicleEventEntity(
                eventEntryDto.getEventType().getValue(),
                eventEntryDto.getEntryTime(),
                savedVehicle);

        vehicleEventRepository.save(vehicleEvent);
        TicketEntity newTicket = new TicketEntity(savedVehicle);
        ticketRepository.save(newTicket);
        return new WebhookEventResponseDto("Vehicle entry registered successfully");
    }

    @Transactional
    public WebhookEventResponseDto registerVehicleParking(WebhookEventParkedDto eventParkedDto) {
        LOGGER.info("Registering vehicle parking: {}", eventParkedDto);

        SpotEntity spot = spotRepository.findByLatitudeAndLongitude(
                eventParkedDto.getLatitude(), eventParkedDto.getLongitude()
        ).orElseThrow(() -> {
            LOGGER.warn("No spot found at the given location: {}, {}",
                    eventParkedDto.getLatitude(), eventParkedDto.getLongitude());
            return new SpotNotFoundException("No parking spot found at the provided coordinates.");
        });

        if (spot.isOccupied()) {
            LOGGER.warn("Spot is already occupied. Cannot register vehicle parking.");
            throw new SpotAlreadyOccupiedException("Parking not allowed. Spot is already occupied.");
        }

        spot.setOccupied(true);

        VehicleEntity vehicle = findVehicleByLicensePlate(eventParkedDto.getLicensePlate());

        VehicleEventEntity vehicleEvent = new VehicleEventEntity(
                eventParkedDto.getEventType().getValue(),
                LocalDateTime.now(),
                vehicle,
                spot
        );
        vehicleEventRepository.save(vehicleEvent);

        TicketEntity ticket = findByAValidTicketAndVehicle(vehicle, eventParkedDto.getLicensePlate());
        SectorEntity sector = spot.getSector();
        ticket.setSpot(spot);
        ticket.setParkingTime(LocalDateTime.now());
        ticket.setPriceRate(BigDecimal.valueOf(sector.toDomain().getDynamicPricingRate()));
        return new WebhookEventResponseDto("Vehicle parked successfully");
    }

    @Transactional
    public WebhookEventResponseDto registerVehicleExit(WebhookEventExitDto eventExitDto) {
        LOGGER.info("Registering vehicle exit: {}", eventExitDto);

        VehicleEntity vehicle = findVehicleByLicensePlate(eventExitDto.getLicensePlate());

        VehicleEventEntity vehicleEvent = new VehicleEventEntity(
                eventExitDto.getEventType().getValue(),
                eventExitDto.getExitTime(),
                vehicle
        );
        vehicleEventRepository.save(vehicleEvent);

        TicketEntity ticket = findByAValidTicketAndVehicle(vehicle, eventExitDto.getLicensePlate());
        Ticket ticketDomain = ticket.toDomain();
        ticketDomain.calculateParkingDuration();
        ticketDomain.getSpot().setOccupied(false);
        ticket.fromDomain(ticketDomain);

        revenueService.addRevenueWhenSpotIsFree(
                eventExitDto.getExitTime().toLocalDate(),
                ticket.getSpot().getSector().getSectorCode(),
                ticket.getTotalPrice()
        );

        return new WebhookEventResponseDto("Vehicle exit registered successfully");
    }

    public PlateStatusResponseDto getPlateStatus(PlateStatusRequestDto plateStatusRequestDto) {
        LOGGER.info("Getting vehicle status for license plate: {}", plateStatusRequestDto.licensePlate());

        VehicleEntity vehicle = findVehicleByLicensePlate(plateStatusRequestDto.licensePlate());
        TicketEntity ticket = findByAValidTicketAndVehicle(vehicle, plateStatusRequestDto.licensePlate());

        return new PlateStatusResponseDto(
                vehicle.getLicensePlate(),
                ticket.toDomain().calculateTotalPrice().toString(),
                ticket.getEntryTime(),
                ticket.getParkingTime()
        );
    }

    private VehicleEntity findVehicleByLicensePlate(String eventExitDto) {
        return vehicleRepository.findByLicensePlate(eventExitDto)
                .orElseThrow(() -> {
                    LOGGER.warn("No vehicle found with license plate: {}", eventExitDto);
                    return new VechicleNotFoundException("Vehicle not found.");
                });
    }

    private TicketEntity findByAValidTicketAndVehicle(VehicleEntity vehicle, String eventExitDto) {
        return ticketRepository.findByValidAndVehicle(true, vehicle)
                .orElseThrow(() -> {
                    LOGGER.warn("No valid ticket found for vehicle with license plate: {}", eventExitDto);
                    return new TicketNotFoundException("No valid ticket found for the vehicle.");
                });
    }
}
