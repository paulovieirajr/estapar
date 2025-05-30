package io.github.paulovieirajr.estapar.service.revenue;

import io.github.paulovieirajr.estapar.adapter.dto.revenue.RevenueRequestDto;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.RevenueEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.RevenueRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.SectorRepository;
import io.github.paulovieirajr.estapar.service.exception.revenue.RevenueNotFoundException;
import io.github.paulovieirajr.estapar.service.exception.sector.SectorNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class RevenueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevenueService.class);

    private final RevenueRepository revenueRepository;
    private final SectorRepository sectorRepository;

    public RevenueService(RevenueRepository revenueRepository, SectorRepository sectorRepository) {
        this.revenueRepository = revenueRepository;
        this.sectorRepository = sectorRepository;
    }

    public Optional<RevenueEntity> fetchRevenueBySector(RevenueRequestDto revenueRequestDto) {
        LOGGER.info("Fetching revenue by sector code");
        SectorEntity sector = recoverSectorByCode(revenueRequestDto.sectorCode());
        return revenueRepository.findByDateAndSector(revenueRequestDto.date(), sector);
    }

    public void addRevenueWhenSpotIsFree(LocalDate exitDate, String sectorCode, BigDecimal amount) {
        LOGGER.info("Adding a new revenue when spot is free for date: {}", exitDate);

        SectorEntity sector = recoverSectorByCode(sectorCode);
        Optional<RevenueEntity> recoveredRevenue = revenueRepository.findByDateAndSector(exitDate, sector);

        recoveredRevenue.ifPresentOrElse(
                revenueEntity -> {
                    LOGGER.info("Revenue already exists for date: {}. Updating revenue.", exitDate);
                    updateExistingRevenue(recoveredRevenue, amount);
                },
                () -> {
                    LOGGER.info("No revenue found for date: {}. Creating new revenue.", exitDate);
                    createNewRevenue(exitDate, sector, amount);
                }
        );
    }

    private void createNewRevenue(LocalDate exitDate, SectorEntity sector, BigDecimal amount) {
        RevenueEntity newRevenue = new RevenueEntity(exitDate, amount, sector);
        revenueRepository.save(newRevenue);
    }

    private void updateExistingRevenue(Optional<RevenueEntity> recoveredRevenue, BigDecimal amount) {
           RevenueEntity revenueEntity = recoveredRevenue.orElseThrow(() -> new RevenueNotFoundException(
                   "Revenue not found for the given date and sector")
           );
           addRevenueAmount(revenueEntity, amount);
           revenueRepository.save(revenueEntity);
       }

    public void addRevenueAmount(RevenueEntity revenue, BigDecimal amount) {
        revenue.setAmount(revenue.getAmount().add(amount));
    }

    private SectorEntity recoverSectorByCode(String sectorCode) {
        return sectorRepository.findBySectorCode(sectorCode)
                .orElseThrow(() -> new SectorNotFoundException(
                        "Sector not found with code: " + sectorCode)
                );
    }
}
