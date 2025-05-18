package io.github.paulovieirajr.estapar.domain.model;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public final class Sector {

    public static final double TWENTY_FIVE_PERCENT_OCCUPANCY_RATE = 0.25;
    public static final double TEN_PERCENT_DISCOUNT_RATE = 0.9;
    public static final double FIFTY_PERCENT_OCCUPANCY_RATE = 0.5;
    public static final double ZERO_PERCENT_DISCOUNT_RATE = 1.0;
    public static final double SEVENTY_FIVE_PERCENT_OCCUPANCY_RATE = 0.75;
    public static final double TEN_PERCENT_SURCHARGE_RATE = 1.0;
    public static final double TWENTY_FIVE_PERCENT_SURCHARGE_RATE = 1.25;

    private final String sectorCode;
    private final BigDecimal basePrice;
    private final Integer maxCapacity;
    private final LocalTime openHour;
    private final LocalTime closeHour;
    private final Integer durationLimitMinutes;
    private final Garage garage;
    private final List<Spot> spots;

    public Sector(String sectorCode, BigDecimal basePrice, Integer maxCapacity, LocalTime openHour,
                  LocalTime closeHour, Integer durationLimitMinutes, Garage garage, List<Spot> spots) {
        this.sectorCode = sectorCode;
        this.basePrice = basePrice;
        this.maxCapacity = maxCapacity;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.durationLimitMinutes = durationLimitMinutes;
        this.garage = garage;
        this.spots = spots;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public boolean canEntry(LocalTime entryTime) {
        return !entryTime.isBefore(this.openHour) && !entryTime.isAfter(this.openHour);
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
        long occupiedSpots = spots.stream().filter(Spot::isOccupied).count();
        return (double) occupiedSpots / maxCapacity;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public LocalTime getOpenHour() {
        return openHour;
    }

    public LocalTime getCloseHour() {
        return closeHour;
    }

    public Integer getDurationLimitMinutes() {
        return durationLimitMinutes;
    }

    public Garage getGarage() {
        return garage;
    }

    public List<Spot> getSpots() {
        return spots;
    }
}
