package io.github.paulovieirajr.estapar.service.sector;

import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SpotEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.SectorRepository;
import io.github.paulovieirajr.estapar.service.exception.sector.SectorNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class SectorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SectorService.class);

    public static final double TWENTY_FIVE_PERCENT_OCCUPANCY_RATE = 0.25;
    public static final double TEN_PERCENT_DISCOUNT_RATE = 0.9;
    public static final double FIFTY_PERCENT_OCCUPANCY_RATE = 0.5;
    public static final double ZERO_PERCENT_DISCOUNT_RATE = 1.0;
    public static final double SEVENTY_FIVE_PERCENT_OCCUPANCY_RATE = 0.75;
    public static final double TEN_PERCENT_SURCHARGE_RATE = 1.0;
    public static final double TWENTY_FIVE_PERCENT_SURCHARGE_RATE = 1.25;
    public static final double MAXIMUM_OCCUPANCY_RATE = 1.0;

    private final SectorRepository sectorRepository;

    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public boolean isSectorTotallyOccupied(String sectorCode) {
        LOGGER.info("Checking if sector is totally occupied: {}", sectorCode);
        return sectorRepository.findBySectorCode(sectorCode)
                .map(this::isSectorTotallyOccupied)
                .orElseThrow(() -> new SectorNotFoundException("Sector not found with code: " + sectorCode));
    }

    public boolean areAllSectorsFullOrClosed(LocalTime entryTime) {
        LOGGER.info("Checking if all sectors are totally occupied");
        List<SectorEntity> sectors = sectorRepository.findAll();

        boolean existsAvailableSector = sectors.stream()
                .anyMatch(sector -> {
                    boolean isOpen = canEntry(entryTime, sector);
                    boolean hasVacancy = !isSectorTotallyOccupied(sector.getSectorCode());
                    return isOpen && hasVacancy;
                });

        return !existsAvailableSector;
    }

    public boolean canEntry(LocalTime entryTime, SectorEntity sector) {
        return !entryTime.isBefore(sector.getOpenHour()) && !entryTime.isAfter(sector.getCloseHour());
    }

    public double getDynamicPricingRate(SectorEntity sector) {
        double occupancy = getOccupancyRate(sector);
        if (occupancy < TWENTY_FIVE_PERCENT_OCCUPANCY_RATE) return TEN_PERCENT_DISCOUNT_RATE;
        if (occupancy < FIFTY_PERCENT_OCCUPANCY_RATE) return ZERO_PERCENT_DISCOUNT_RATE;
        if (occupancy < SEVENTY_FIVE_PERCENT_OCCUPANCY_RATE) return TEN_PERCENT_SURCHARGE_RATE;
        return TWENTY_FIVE_PERCENT_SURCHARGE_RATE;
    }

    public double getOccupancyRate(SectorEntity sector) {
        if (sector.getMaxCapacity() == null || sector.getMaxCapacity() <= 0) {
            return 0.0;
        }
        long occupiedSpots = sector.getSpots().stream().filter(SpotEntity::isOccupied).count();
        return (double) occupiedSpots / sector.getMaxCapacity();
    }

    private boolean isSectorTotallyOccupied(SectorEntity sector) {
        return getOccupancyRate(sector) >= MAXIMUM_OCCUPANCY_RATE;
    }
}
