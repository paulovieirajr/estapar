package io.github.paulovieirajr.estapar.domain.model;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public record Sector(
        String sectorCode,
        BigDecimal basePrice,
        Integer maxCapacity,
        LocalTime openHour,
        LocalTime closeHour,
        Integer durationLimitMinutes,
        Garage garage, List<Spot> spots
) {

    public static final double TWENTY_FIVE_PERCENT_OCCUPANCY_RATE = 0.25;
    public static final double TEN_PERCENT_DISCOUNT_RATE = 0.9;
    public static final double FIFTY_PERCENT_OCCUPANCY_RATE = 0.5;
    public static final double ZERO_PERCENT_DISCOUNT_RATE = 1.0;
    public static final double SEVENTY_FIVE_PERCENT_OCCUPANCY_RATE = 0.75;
    public static final double TEN_PERCENT_SURCHARGE_RATE = 1.0;
    public static final double TWENTY_FIVE_PERCENT_SURCHARGE_RATE = 1.25;

    public boolean canEntry(LocalTime entryTime) {
        return !entryTime.isBefore(openHour()) && !entryTime.isAfter(closeHour());
    }

    public double getDynamicPricingRate() {
        double occupancy = getOccupancyRate();
        if (occupancy < TWENTY_FIVE_PERCENT_OCCUPANCY_RATE) return TEN_PERCENT_DISCOUNT_RATE;
        if (occupancy < FIFTY_PERCENT_OCCUPANCY_RATE) return ZERO_PERCENT_DISCOUNT_RATE;
        if (occupancy < SEVENTY_FIVE_PERCENT_OCCUPANCY_RATE) return TEN_PERCENT_SURCHARGE_RATE;
        return TWENTY_FIVE_PERCENT_SURCHARGE_RATE;
    }

    public double getOccupancyRate() {
        if (maxCapacity == null || maxCapacity <= 0) {
            return 0.0;
        }
        long occupiedSpots = spots.stream().filter(Spot::occupied).count();
        return (double) occupiedSpots / maxCapacity;
    }

    public void addSpot(Spot spot) {
        this.spots.add(spot);
    }

    public void removeSpot(Spot spot) {
        this.spots.remove(spot);
    }
}
