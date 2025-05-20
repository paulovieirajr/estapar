package io.github.paulovieirajr.estapar.service.spot;

import io.github.paulovieirajr.estapar.adapter.dto.spot.SpotStatusResponseDto;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SpotEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.TicketEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.SpotRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.TicketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpotServiceTest {

    public static final double LATITUDE = -23.561684;
    public static final double LONGITUDE = -46.655981;
    public static final LocalDateTime PARKING_TIME = LocalDateTime.now();
    public static final LocalDateTime ENTRY_TIME = PARKING_TIME.minusMinutes(5);

    @Mock
    private SpotRepository spotRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private SpotService spotService;

    @Test
    @DisplayName("Should return spot status when spot is occupied")
    void shouldReturnSpotStatusWhenSpotIsOccupied() {
        Double latitude = LATITUDE;
        Double longitude = LONGITUDE;
        LocalDateTime entryTime = ENTRY_TIME;
        LocalDateTime parkingTime = PARKING_TIME;

        SpotEntity spot = mock(SpotEntity.class);
        TicketEntity ticket = mock(TicketEntity.class);

        when(spotRepository.findByLatitudeAndLongitude(latitude, longitude)).thenReturn(Optional.of(spot));
        when(spot.isOccupied()).thenReturn(true);
        when(ticketRepository.findBySpot(spot)).thenReturn(Optional.of(ticket));
        when(ticket.getEntryTime()).thenReturn(entryTime);
        when(ticket.getParkingTime()).thenReturn(parkingTime);

        Optional<SpotStatusResponseDto> result = spotService.getSpotStatusByLatitudeAndLongitude(latitude, longitude);

        assertTrue(result.isPresent());
        SpotStatusResponseDto dto = result.get();
        assertTrue(dto.occupied());
        assertEquals(entryTime, dto.entryTime());
        assertEquals(parkingTime, dto.timeParked());
    }

    @Test
    @DisplayName("Should return spot status when spot is free")
    void shouldReturnSpotStatusWhenSpotIsFree() {
        Double latitude = LATITUDE;
        Double longitude = LONGITUDE;
        SpotEntity spot = mock(SpotEntity.class);

        when(spotRepository.findByLatitudeAndLongitude(latitude, longitude)).thenReturn(Optional.of(spot));
        when(spot.isOccupied()).thenReturn(false);

        Optional<SpotStatusResponseDto> result = spotService.getSpotStatusByLatitudeAndLongitude(latitude, longitude);

        assertTrue(result.isPresent());
        SpotStatusResponseDto dto = result.get();
        assertFalse(dto.occupied());
        assertNull(dto.entryTime());
        assertNull(dto.timeParked());
    }

    @Test
    @DisplayName("Should return empty when spot is not found")
    void shouldReturnEmptyWhenSpotIsNotFound() {
        Double latitude = LATITUDE;
        Double longitude = LONGITUDE;

        when(spotRepository.findByLatitudeAndLongitude(latitude, longitude)).thenReturn(Optional.empty());

        Optional<SpotStatusResponseDto> result = spotService.getSpotStatusByLatitudeAndLongitude(latitude, longitude);

        assertFalse(result.isPresent());
    }
}