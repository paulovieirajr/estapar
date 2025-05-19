package io.github.paulovieirajr.estapar.domain.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public final class Ticket {

    private final UUID id;
    private BigDecimal totalPrice;
    private final BigDecimal priceRate;
    private boolean valid;
    private final Spot spot;
    private final LocalDateTime entryTime;
    private final LocalDateTime parkingTime;
    private LocalDateTime exitTime;

    public Ticket(UUID id, BigDecimal totalPrice, BigDecimal priceRate, boolean valid,
                  Spot spot, LocalDateTime entryTime, LocalDateTime parkingTime, LocalDateTime exitTime) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.priceRate = priceRate;
        this.valid = valid;
        this.spot = spot;
        this.entryTime = entryTime;
        this.parkingTime = parkingTime;
        this.exitTime = exitTime;
    }

    public Duration calculateParkingDuration() {
        if (exitTime != null) {
            return Duration.between(entryTime, exitTime);
        }
        return Duration.ZERO;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
        this.totalPrice = calculateTotalPrice();
        this.valid = false;
    }

    public BigDecimal calculateTotalPrice() {
        if (exitTime != null) {
            return BigDecimal.ZERO;
        }

        BigDecimal basePrice = spot.getSector().getBasePrice();
        Duration ticketDuration = calculateParkingDuration();
        double hours = Math.ceil((double) ticketDuration.toMinutes() / 60);

        return basePrice
                .multiply(BigDecimal.valueOf(hours))
                .multiply(priceRate);
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getPriceRate() {
        return priceRate;
    }

    public boolean isValid() {
        return valid;
    }

    public Spot getSpot() {
        return spot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getParkingTime() {
        return parkingTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }
}
