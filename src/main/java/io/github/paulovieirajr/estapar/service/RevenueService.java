package io.github.paulovieirajr.estapar.service;

import io.github.paulovieirajr.estapar.adapter.dto.revenue.RevenueRequestDto;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.RevenueEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.RevenueRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.SectorRepository;
import io.github.paulovieirajr.estapar.service.exception.revenue.RevenueAlreadyExistsException;
import io.github.paulovieirajr.estapar.service.exception.sector.SectorNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

    public List<RevenueEntity> fetchRevenueBySectorBetweenDates(LocalDate startDate, LocalDate endDate, String sectorCode) {
        LOGGER.info("Fetching revenue by sector code between dates");
        SectorEntity sector = recoverSectorByCode(sectorCode);
        return revenueRepository.findByDateBetweenAndSector(startDate, endDate, sector);
    }

    public void addRevenueWhenSpotIsFree(LocalDate exitDate, String sectorCode, BigDecimal amount) {
        LOGGER.info("Adding a new revenue when spot is free");
        SectorEntity sector = recoverSectorByCode(sectorCode);

        revenueRepository.findByDateAndSector(exitDate, sector)
                .ifPresent(revenue -> {
                    LOGGER.warn("Revenue already exists for date: {} and sector: {}", exitDate, sectorCode);
                    throw new RevenueAlreadyExistsException(
                            "Revenue already exists for date: " + exitDate + " and sector: " + sectorCode);
                });

        RevenueEntity revenue = new RevenueEntity(exitDate, amount, sector);
        revenueRepository.save(revenue);
    }

    private SectorEntity recoverSectorByCode(String sectorCode) {
        return sectorRepository.findBySectorCode(sectorCode)
                .orElseThrow(() -> new SectorNotFoundException(
                        "Sector not found with code: " + sectorCode)
                );
    }
}
