package io.github.paulovieirajr.estapar.service.sector;

import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SpotEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.SectorRepository;
import io.github.paulovieirajr.estapar.service.exception.sector.SectorNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SectorServiceTest {

    public static final String SECTOR_CODE_A = "A";
    public static final String SECTOR_CODE_B = "B";
    public static final LocalTime OPEN_HOUR = LocalTime.of(8, 0);
    public static final LocalTime CLOSE_HOUR = LocalTime.of(18, 0);

    @Mock
    private SectorRepository sectorRepository;

    @InjectMocks
    private SectorService sectorService;

    @Test
    @DisplayName("Should return true when sector is totally occupied")
    void shouldReturnTrueWhenSectorIsOccupied() {
        String code = SECTOR_CODE_A;
        SectorEntity sector = buildSectorEntity(code, 2, 2);
        sector.getSpots().forEach(spot -> spot.setOccupied(true));
        when(sectorRepository.findBySectorCode(code)).thenReturn(Optional.of(sector));

        boolean result = sectorService.isSectorTotallyOccupied(code);

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when sector is not totally occupied")
    void shouldReturnFalseWhenSectorIsNotOccupied() {
        String code = SECTOR_CODE_A;
        SectorEntity sector = buildSectorEntity(code, 2, 1);
        sector.getSpots().get(0).setOccupied(true);
        sector.getSpots().get(1).setOccupied(false);
        when(sectorRepository.findBySectorCode(code)).thenReturn(Optional.of(sector));

        boolean result = sectorService.isSectorTotallyOccupied(code);

        assertFalse(result);
    }

    @Test
    @DisplayName("Should throw SectorNotFoundException when sector is not found")
    void shouldThrowSectorNotFoundExceptionWhenSectorIsNotFound() {
        String code = SECTOR_CODE_B;
        when(sectorRepository.findBySectorCode(code)).thenReturn(Optional.empty());

        assertThrows(SectorNotFoundException.class, () -> sectorService.isSectorTotallyOccupied(code));
    }

    @Test
    @DisplayName("Should return true when all sectors are full or closed")
    void shouldReturnTrueWhenAllSectorsFullOrClosed() {
        SectorEntity full = buildSectorEntity(SECTOR_CODE_A, 2, 2);
        full.getSpots().forEach(spot -> spot.setOccupied(true));

        List<SectorEntity> list = Collections.singletonList(full);
        when(sectorRepository.findAll()).thenReturn(list);
        when(sectorRepository.findBySectorCode(SECTOR_CODE_A)).thenReturn(Optional.of(full));

        boolean result = sectorService.areAllSectorsFullOrClosed(LocalTime.of(10, 0));
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when one sector is available")
    void shouldReturnFalseWhenOneSectorIsAvailable() {
        SectorEntity full = buildSectorEntity(SECTOR_CODE_A, 2, 2);
        full.getSpots().forEach(spot -> spot.setOccupied(true));
        SectorEntity notFull = buildSectorEntity(SECTOR_CODE_B, 2, 1);
        notFull.getSpots().get(0).setOccupied(true);
        notFull.getSpots().get(1).setOccupied(false);

        List<SectorEntity> list = Arrays.asList(full, notFull);
        when(sectorRepository.findAll()).thenReturn(list);
        when(sectorRepository.findBySectorCode(SECTOR_CODE_A)).thenReturn(Optional.of(full));
        when(sectorRepository.findBySectorCode(SECTOR_CODE_B)).thenReturn(Optional.of(notFull));

        boolean result = sectorService.areAllSectorsFullOrClosed(LocalTime.of(10, 0));
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return true when all sectors are closed")
    void shouldReturnTrueWhenAllSectorsClosed() {
        SectorEntity closed = buildSectorEntity(SECTOR_CODE_A, 2, 0);
        closed.setOpenHour(OPEN_HOUR);
        closed.setCloseHour(CLOSE_HOUR);
        when(sectorRepository.findAll()).thenReturn(Collections.singletonList(closed));
        when(sectorRepository.findBySectorCode(SECTOR_CODE_A)).thenReturn(Optional.of(closed));

        LocalTime entryTimeNotAllowed = LocalTime.of(19, 0);
        boolean result = sectorService.areAllSectorsFullOrClosed(entryTimeNotAllowed);
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return true when inside interval")
    void shouldReturnTrueWhenInsideInterval() {
        SectorEntity sector = buildSectorEntity(SECTOR_CODE_A, 2, 1);
        sector.setOpenHour(OPEN_HOUR);
        sector.setCloseHour(CLOSE_HOUR);

        assertTrue(sectorService.canEntry(LocalTime.of(10, 0), sector));
    }

    @Test
    @DisplayName("Should return false when before open hour")
    void shouldReturnFalseWhenBeforeOpen() {
        SectorEntity sector = buildSectorEntity(SECTOR_CODE_A, 2, 1);
        sector.setOpenHour(OPEN_HOUR);
        sector.setCloseHour(CLOSE_HOUR);

        LocalTime entryTimeBeforeOpening = LocalTime.of(7, 59);
        assertFalse(sectorService.canEntry(entryTimeBeforeOpening, sector));
    }

    @Test
    @DisplayName("Should return false when after close hour")
    void shouldReturnFalseWhenAfterClose() {
        SectorEntity sector = buildSectorEntity(SECTOR_CODE_A, 2, 1);
        sector.setOpenHour(OPEN_HOUR);
        sector.setCloseHour(CLOSE_HOUR);

        LocalTime entryTimeNotAllowed = LocalTime.of(18, 1);
        assertFalse(sectorService.canEntry(entryTimeNotAllowed, sector));
    }

    @Test
    @DisplayName("Should give 10% discount when occupancy is less than 25%")
    void shouldGiveTenPercentDiscountWhenLessThan25PercentOccupied() {
        SectorEntity sector = buildSectorEntity(SECTOR_CODE_A, 10, 1);
        sector.getSpots().getFirst().setOccupied(true);
        double rate = sectorService.getDynamicPricingRate(sector);
        assertEquals(SectorService.TEN_PERCENT_DISCOUNT_RATE, rate);
    }

    @Test
    @DisplayName("Should give 0% discount when occupancy is between 25% and 50%")
    void shouldGiveZeroPercentDiscountWhenBetween25And50PercentOccupied() {
        SectorEntity sector = buildSectorEntity(SECTOR_CODE_A, 10, 4);
        for (int i = 0; i < 4; i++) sector.getSpots().get(i).setOccupied(true);
        double rate = sectorService.getDynamicPricingRate(sector);
        assertEquals(SectorService.ZERO_PERCENT_DISCOUNT_RATE, rate);
    }

    @Test
    @DisplayName("Should give 10% surcharge when occupancy is between 50% and 75%")
    void shouldGiveTenPercentSurchargeWhenBetween50And75PercentOccupied() {
        SectorEntity sector = buildSectorEntity(SECTOR_CODE_A, 10, 7);
        for (int i = 0; i < 7; i++) sector.getSpots().get(i).setOccupied(true);
        double rate = sectorService.getDynamicPricingRate(sector);
        assertEquals(SectorService.TEN_PERCENT_SURCHARGE_RATE, rate);
    }

    @Test
    @DisplayName("Should give 25% surcharge when occupancy is above 75%")
    void shouldGiveTwentyFivePercentSurchargeWhenAbove75PercentOccupied() {
        SectorEntity sector = buildSectorEntity(SECTOR_CODE_A, 8, 7);
        for (int i = 0; i < 7; i++) sector.getSpots().get(i).setOccupied(true);
        double rate = sectorService.getDynamicPricingRate(sector);
        assertEquals(SectorService.TWENTY_FIVE_PERCENT_SURCHARGE_RATE, rate);
    }

    private SectorEntity buildSectorEntity(String code, int capacity, int occupied) {
        SectorEntity sector = new SectorEntity(
                code,
                BigDecimal.valueOf(10.0),
                capacity,
                OPEN_HOUR,
                CLOSE_HOUR,
                60
        );
        List<SpotEntity> spots = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            SpotEntity spot = new SpotEntity();
            spot.setOccupied(i < occupied);
            spots.add(spot);
        }
        for (SpotEntity spot : spots) {
            sector.addSpot(spot);
        }
        return sector;
    }
}