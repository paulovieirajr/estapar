package io.github.paulovieirajr.estapar.adapter.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sector")
public class SectorEntity {

    @Id
    @Column(name = "sector_code", nullable = false, length = 5)
    private String sectorCode;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "open_hour", nullable = false)
    private LocalTime openHour;

    @Column(name = "close_hour", nullable = false)
    private LocalTime closeHour;

    @Column(name = "duration_limit_minutes", nullable = false)
    private Integer durationLimitMinutes;

    @ManyToOne
    @JoinColumn(
            name = "garage_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_sector_garage")
    )
    private GarageEntity garage;

    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SpotEntity> spots = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public SectorEntity() {
    }

    public SectorEntity(String sectorCode, BigDecimal basePrice, Integer maxCapacity,
                        LocalTime openHour, LocalTime closeHour, Integer durationLimitMinutes) {
        this.sectorCode = sectorCode;
        this.basePrice = basePrice;
        this.maxCapacity = maxCapacity;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.durationLimitMinutes = durationLimitMinutes;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    public void setSectorCode(String sector) {
        this.sectorCode = sector;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public LocalTime getOpenHour() {
        return openHour;
    }

    public void setOpenHour(LocalTime openHour) {
        this.openHour = openHour;
    }

    public LocalTime getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(LocalTime closeHour) {
        this.closeHour = closeHour;
    }

    public Integer getDurationLimitMinutes() {
        return durationLimitMinutes;
    }

    public void setDurationLimitMinutes(Integer durationLimitMinutes) {
        this.durationLimitMinutes = durationLimitMinutes;
    }

    public GarageEntity getGarage() {
        return garage;
    }

    public void setGarage(GarageEntity garage) {
        this.garage = garage;
    }

    public List<SpotEntity> getSpots() {
        return spots;
    }

    public void addSpot(SpotEntity spotEntity) {
        if (spotEntity == null) {
            throw new IllegalArgumentException("Spot cannot be null");
        }
        spotEntity.setSector(this);
        this.spots.add(spotEntity);
    }

    public void removeSpot(SpotEntity spotEntity) {
        spotEntity.setSector(null);
        this.spots.remove(spotEntity);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
