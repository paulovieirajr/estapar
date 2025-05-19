package io.github.paulovieirajr.estapar.adapter.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ticket")
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "priceRate")
    private BigDecimal priceRate;

    @Column(name = "valid")
    private Boolean valid;

    @CreationTimestamp
    @Column(name = "entry_time", nullable = false, updatable = false)
    private LocalDateTime entryTime;

    @Column(name = "parking_time")
    private LocalDateTime parkingTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @ManyToOne
    @JoinColumn(
            name = "vehicle_id",
            foreignKey = @ForeignKey(name = "fk_ticket_vehicle")
    )
    private VehicleEntity vehicle;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "spot_id",
            foreignKey = @ForeignKey(name = "fk_ticket_spot")
    )
    private SpotEntity spot;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public TicketEntity() {
    }

    public TicketEntity(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getPriceRate() {
        return priceRate;
    }

    public void setPriceRate(BigDecimal rate) {
        this.priceRate = rate;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getParkingTime() {
        return parkingTime;
    }

    public void setParkingTime(LocalDateTime parkingTime) {
        this.parkingTime = parkingTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }

    public SpotEntity getSpot() {
        return spot;
    }

    public void setSpot(SpotEntity spot) {
        this.spot = spot;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
