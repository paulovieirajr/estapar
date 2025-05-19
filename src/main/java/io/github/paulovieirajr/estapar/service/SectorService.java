package io.github.paulovieirajr.estapar.service;

import io.github.paulovieirajr.estapar.adapter.persistence.repository.SectorRepository;
import io.github.paulovieirajr.estapar.service.exception.sector.SectorNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SectorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SectorService.class);

    private final SectorRepository sectorRepository;

    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public double getTotalSectorOccupancyRate(String sectorCode) {
        LOGGER.info("Calculating total occupancy rate for sector: {}", sectorCode);
        return sectorRepository.findBySectorCode(sectorCode)
                .map(sector -> sector.toDomain().getOccupancyRate())
                .orElseThrow(() -> new SectorNotFoundException("Sector not found with code: " + sectorCode));
    }

    public boolean isSectorTotallyOccupied(String sectorCode) {
        LOGGER.info("Checking if sector is totally occupied: {}", sectorCode);
        return sectorRepository.findBySectorCode(sectorCode)
                .map(sector -> sector.toDomain().isTotallyOccupied())
                .orElseThrow(() -> new SectorNotFoundException("Sector not found with code: " + sectorCode));
    }

    public boolean areAllSectorsTotallyOccupied() {
        LOGGER.info("Checking if all sectors are totally occupied");
        return sectorRepository.findAll().stream()
                .allMatch(sector -> sector.toDomain().isTotallyOccupied());
    }
}
