package io.github.paulovieirajr.estapar.service.vehicle;

import io.github.paulovieirajr.estapar.adapter.dto.vehicle.LicensePlateRequestDto;
import io.github.paulovieirajr.estapar.adapter.dto.vehicle.LicensePlateResponseDto;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.enums.EventType;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEventEntryDto;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEventExitDto;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEventParkedDto;
import io.github.paulovieirajr.estapar.adapter.dto.webhook.event.WebhookEventResponseDto;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SpotEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.TicketEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.VehicleEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.SpotRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.TicketRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.VehicleEventRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.VehicleRepository;
import io.github.paulovieirajr.estapar.service.exception.sector.SectorAlreadyFullException;
import io.github.paulovieirajr.estapar.service.exception.spot.SpotAlreadyOccupiedException;
import io.github.paulovieirajr.estapar.service.exception.spot.SpotNotFoundException;
import io.github.paulovieirajr.estapar.service.exception.vehicle.VechicleNotFoundException;
import io.github.paulovieirajr.estapar.service.exception.vehicle.VehicleAlreadyExistsException;
import io.github.paulovieirajr.estapar.service.revenue.RevenueService;
import io.github.paulovieirajr.estapar.service.sector.SectorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private SectorService sectorService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private VehicleEventRepository vehicleEventRepository;

    @Mock
    private SpotRepository spotRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private RevenueService revenueService;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    @DisplayName("Should register vehicle entry successfully")
    void shouldRegisterVehicleEntry_success() {
        String plate = "ABC1234";
        LocalDateTime now = LocalDateTime.now();
        WebhookEventEntryDto entryDto = new WebhookEventEntryDto(plate, EventType.ENTRY, now);

        when(sectorService.areAllSectorsFullOrClosed(any())).thenReturn(false);
        when(vehicleRepository.findByLicensePlate(plate)).thenReturn(Optional.empty());
        when(vehicleRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(vehicleEventRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(ticketRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        WebhookEventResponseDto response = vehicleService.registerVehicleEntry(entryDto);

        assertEquals("Vehicle entry registered successfully", response.message());
        verify(vehicleRepository).save(any());
        verify(vehicleEventRepository).save(any());
        verify(ticketRepository).save(any());
    }

    @Test
    @DisplayName("Should throw exception when all sectors are full or closed")
    void shouldThrowException_whenAllSectorsFullOrClosed() {
        String plate = "DEF5678";
        LocalDateTime now = LocalDateTime.now();
        WebhookEventEntryDto entryDto = new WebhookEventEntryDto(plate, EventType.ENTRY, now);

        when(sectorService.areAllSectorsFullOrClosed(any())).thenReturn(true);

        assertThrows(SectorAlreadyFullException.class, () -> vehicleService.registerVehicleEntry(entryDto));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when vehicle already exists")
    void shouldThrowException_whenVehicleAlreadyExists() {
        String plate = "XYZ9876";
        LocalDateTime now = LocalDateTime.now();
        WebhookEventEntryDto entryDto = new WebhookEventEntryDto(plate, EventType.ENTRY, now);

        when(sectorService.areAllSectorsFullOrClosed(any())).thenReturn(false);
        when(vehicleRepository.findByLicensePlate(plate)).thenReturn(Optional.of(newVehicle(plate)));

        assertThrows(VehicleAlreadyExistsException.class, () -> vehicleService.registerVehicleEntry(entryDto));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should register vehicle parking successfully")
    void shouldRegisterVehicleParking_success() {
        String plate = "GHI4567";
        double lat = 11.1, lon = 22.2;
        LocalDateTime entry = LocalDateTime.now().minusMinutes(61);
        LocalDateTime parking = LocalDateTime.now().minusMinutes(30);
        BigDecimal priceRate = BigDecimal.valueOf(1.5);
        SectorEntity sector = newSector(BigDecimal.valueOf(10.0));
        SpotEntity spot = newSpot(false, sector);
        VehicleEntity vehicle = newVehicle(plate);
        TicketEntity ticket = newTicket(vehicle, null, entry, null, priceRate);

        WebhookEventParkedDto parkedDto = new WebhookEventParkedDto(plate, EventType.PARKED, lat, lon);

        when(spotRepository.findByLatitudeAndLongitude(lat, lon)).thenReturn(Optional.of(spot));
        when(vehicleRepository.findByLicensePlate(plate)).thenReturn(Optional.of(vehicle));
        when(ticketRepository.findByValidAndVehicle(true, plate)).thenReturn(Optional.of(ticket));
        when(sectorService.getDynamicPricingRate(sector)).thenReturn(2.0);
        when(vehicleEventRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(ticketRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        WebhookEventResponseDto response = vehicleService.registerVehicleParking(parkedDto);

        assertEquals("Vehicle parked successfully", response.message());
        assertTrue(spot.isOccupied());
        verify(vehicleEventRepository).save(any());
        verify(ticketRepository).save(any());
    }

    @Test
    @DisplayName("Should throw exception when vehicle not found")
    void shouldThrowException_whenVehicleNotFound() {
        String plate = "NOP7890";
        double lat = 33.3, lon = 44.4;
        WebhookEventParkedDto parkedDto = new WebhookEventParkedDto(plate, EventType.PARKED, lat, lon);

        when(spotRepository.findByLatitudeAndLongitude(lat, lon)).thenReturn(Optional.empty());

        assertThrows(SpotNotFoundException.class, () -> vehicleService.registerVehicleParking(parkedDto));
    }

    @Test
    @DisplayName("Should throw exception when spot is already occupied")
    void shouldThrowException_whenSpotOccupied() {
        String plate = "QRS3210";
        double lat = 55.5, lon = 66.6;
        SectorEntity sector = newSector(BigDecimal.valueOf(10.0));
        SpotEntity spot = newSpot(true, sector);
        WebhookEventParkedDto parkedDto = new WebhookEventParkedDto(plate, EventType.PARKED, lat, lon);

        when(spotRepository.findByLatitudeAndLongitude(lat, lon)).thenReturn(Optional.of(spot));

        assertThrows(SpotAlreadyOccupiedException.class, () -> vehicleService.registerVehicleParking(parkedDto));
    }

    @Test
    @DisplayName("Should register vehicle exit successfully")
    void shouldRegisterVehicleExit_success() {
        String plate = "TUV6543";
        LocalDateTime entryTime = LocalDateTime.now().minusHours(2);
        LocalDateTime parkingTime = LocalDateTime.now().minusHours(1);
        LocalDateTime exitTime = LocalDateTime.now();
        SectorEntity sector = newSector(BigDecimal.valueOf(20.0));
        SpotEntity spot = newSpot(true, sector);
        BigDecimal priceRate = BigDecimal.valueOf(1.2);
        VehicleEntity vehicle = newVehicle(plate);
        TicketEntity ticket = newTicket(vehicle, spot, entryTime, parkingTime, priceRate);

        WebhookEventExitDto exitDto = new WebhookEventExitDto(plate, EventType.EXIT, exitTime);

        when(vehicleRepository.findByLicensePlate(plate)).thenReturn(Optional.of(vehicle));
        when(ticketRepository.findByValidAndVehicle(true, plate)).thenReturn(Optional.of(ticket));
        when(vehicleEventRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(ticketRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        WebhookEventResponseDto response = vehicleService.registerVehicleExit(exitDto);

        assertEquals("Vehicle exit registered successfully", response.message());
        assertFalse(ticket.isValid());
        assertNotNull(ticket.getExitTime());
        assertNotNull(ticket.getTotalPrice());
        verify(revenueService).addRevenueWhenSpotIsFree(any(), any(), any());
        verify(ticketRepository).save(any());
    }

    @Test
    @DisplayName("Should throw exception when vehicle not found on exit")
    void shouldThrowException_whenVehicleNotFoundOnExit() {
        String plate = "JKL9876";
        WebhookEventExitDto exitDto = new WebhookEventExitDto(plate, EventType.EXIT, LocalDateTime.now());
        when(vehicleRepository.findByLicensePlate(plate)).thenReturn(Optional.empty());

        assertThrows(VechicleNotFoundException.class, () -> vehicleService.registerVehicleExit(exitDto));
    }

    @Test
    @DisplayName("Should search license plate successfully")
    void shouldSearchLicensePlate_success() {
        String plate = "ZXC1234";
        LocalDateTime entry = LocalDateTime.now().minusHours(4);
        LocalDateTime parking = LocalDateTime.now().minusHours(2);
        SectorEntity sector = newSector(BigDecimal.valueOf(25.0));
        SpotEntity spot = newSpot(false, sector);
        BigDecimal priceRate = BigDecimal.valueOf(2.0);
        VehicleEntity vehicle = newVehicle(plate);
        TicketEntity ticket = newTicket(vehicle, spot, entry, parking, priceRate);

        when(vehicleRepository.findByLicensePlate(plate)).thenReturn(Optional.of(vehicle));
        when(ticketRepository.findByValidAndVehicle(true, plate)).thenReturn(Optional.of(ticket));

        LicensePlateRequestDto req = new LicensePlateRequestDto(plate);
        Optional<LicensePlateResponseDto> res = vehicleService.searchLicensePlate(req);

        assertTrue(res.isPresent());
        assertEquals(plate, res.get().licensePlate());
        assertEquals(entry, res.get().entryTime());
    }

    @Test
    @DisplayName("Should return empty when vehicle not found")
    void shouldReturnEmpty_whenVehicleNotFound() {
        String plate = "NOTFOUND";
        when(vehicleRepository.findByLicensePlate(plate)).thenReturn(Optional.empty());

        LicensePlateRequestDto req = new LicensePlateRequestDto(plate);
        Optional<LicensePlateResponseDto> res = vehicleService.searchLicensePlate(req);

        assertTrue(res.isEmpty());
    }

    private VehicleEntity newVehicle(String plate) {
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setId(UUID.randomUUID());
        vehicle.setLicensePlate(plate);
        return vehicle;
    }

    private SpotEntity newSpot(boolean occupied, SectorEntity sector) {
        SpotEntity spot = new SpotEntity();
        spot.setId(1);
        spot.setLatitude(10.0);
        spot.setLongitude(20.0);
        spot.setOccupied(occupied);
        spot.setSector(sector);
        return spot;
    }

    private SectorEntity newSector(BigDecimal basePrice) {
        SectorEntity sector = new SectorEntity();
        sector.setSectorCode("A");
        sector.setBasePrice(basePrice);
        return sector;
    }

    private TicketEntity newTicket(VehicleEntity vehicle, SpotEntity spot, LocalDateTime entry, LocalDateTime parking, BigDecimal priceRate) {
        TicketEntity ticket = new TicketEntity(vehicle);
        ticket.setId(UUID.randomUUID());
        ticket.setSpot(spot);
        ticket.setValid(true);
        ticket.setEntryTime(entry);
        ticket.setParkingTime(parking);
        ticket.setPriceRate(priceRate);
        return ticket;
    }
}