package io.github.paulovieirajr.estapar.service.revenue;

import io.github.paulovieirajr.estapar.adapter.dto.revenue.RevenueRequestDto;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.RevenueEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.RevenueRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.SectorRepository;
import io.github.paulovieirajr.estapar.service.exception.sector.SectorNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RevenueServiceTest {

    @Mock
    private RevenueRepository revenueRepository;

    @Mock
    private SectorRepository sectorRepository;

    @InjectMocks
    private RevenueService revenueService;

    private SectorEntity mockSector;
    private RevenueEntity mockRevenue;
    private LocalDate date;
    private BigDecimal amount;
    private String sectorCode;

    @BeforeEach
    void setUp() {
        date = LocalDate.now();
        sectorCode = "A";
        mockSector = new SectorEntity();
        mockSector.setSectorCode(sectorCode);
        amount = new BigDecimal("100.00");

        mockRevenue = new RevenueEntity(date, new BigDecimal("50.00"), mockSector);
        mockRevenue.setId(UUID.randomUUID());
    }

    @Test
    @DisplayName("Should fetch revenue by sector when found")
    void shouldFetchRevenueBySector() {
        RevenueRequestDto dto = new RevenueRequestDto(date, sectorCode);

        when(sectorRepository.findBySectorCode(sectorCode)).thenReturn(Optional.of(mockSector));
        when(revenueRepository.findByDateAndSector(date, mockSector)).thenReturn(Optional.of(mockRevenue));

        Optional<RevenueEntity> result = revenueService.fetchRevenueBySector(dto);

        assertTrue(result.isPresent());
        assertEquals(mockRevenue, result.get());
        verify(sectorRepository).findBySectorCode(sectorCode);
        verify(revenueRepository).findByDateAndSector(date, mockSector);
    }

    @Test
    @DisplayName("Should return empty when revenue not found")
    void shouldReturnEmptyWhenRevenueNotFound() {
        RevenueRequestDto dto = new RevenueRequestDto(date, sectorCode);

        when(sectorRepository.findBySectorCode(sectorCode)).thenReturn(Optional.empty());

        assertThrows(SectorNotFoundException.class, () -> revenueService.fetchRevenueBySector(dto));
    }

    @Test
    @DisplayName("Should add amount to existing revenue")
    void shouldAddAmountToExistingRevenue() {
        when(sectorRepository.findBySectorCode(sectorCode)).thenReturn(Optional.of(mockSector));
        when(revenueRepository.findByDateAndSector(date, mockSector)).thenReturn(Optional.of(mockRevenue));

        revenueService.addRevenueWhenSpotIsFree(date, sectorCode, amount);

        assertEquals(new BigDecimal("150.00"), mockRevenue.getAmount());
        verify(revenueRepository).save(mockRevenue);
    }

    @Test
    @DisplayName("Should create new revenue when spot is free and no existing revenue")
    void shouldCreateNewRevenueWhenSpotIsFree() {
        when(sectorRepository.findBySectorCode(sectorCode)).thenReturn(Optional.of(mockSector));
        when(revenueRepository.findByDateAndSector(date, mockSector)).thenReturn(Optional.empty());

        revenueService.addRevenueWhenSpotIsFree(date, sectorCode, amount);

        ArgumentCaptor<RevenueEntity> captor = ArgumentCaptor.forClass(RevenueEntity.class);
        verify(revenueRepository).save(captor.capture());
        RevenueEntity savedEntity = captor.getValue();
        assertEquals(date, savedEntity.getDate());
        assertEquals(amount, savedEntity.getAmount());
        assertEquals(mockSector, savedEntity.getSector());
    }

    @Test
    @DisplayName("Should throw SectorNotFoundException when sector not found")
    void shouldThrowSectorNotFoundExceptionWhenSectorNotFound() {
        when(sectorRepository.findBySectorCode(sectorCode)).thenReturn(Optional.empty());

        assertThrows(SectorNotFoundException.class, () ->
                revenueService.addRevenueWhenSpotIsFree(date, sectorCode, amount)
        );
    }

    @Test
    @DisplayName("Should update existing revenue when it already exists")
    void shouldUpdateExistingRevenue() {
        BigDecimal oldAmount = mockRevenue.getAmount();
        revenueService.addRevenueAmount(mockRevenue, amount);
        assertEquals(oldAmount.add(amount), mockRevenue.getAmount());
    }
}