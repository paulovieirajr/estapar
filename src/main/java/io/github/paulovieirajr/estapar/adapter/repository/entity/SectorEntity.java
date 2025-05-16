package io.github.paulovieirajr.estapar.adapter.repository.entity;

import io.github.paulovieirajr.estapar.domain.model.Sector;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "sector")
public class SectorEntity {

    @Id
    @Column(name = "code", nullable = false, length = 5)
    private String code;

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
    private List<SpotEntity> spots;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public SectorEntity() {
    }

    public SectorEntity(String code, BigDecimal basePrice, Integer maxCapacity, LocalTime openHour,
                        LocalTime closeHour, Integer durationLimitMinutes, GarageEntity garage,
                        List<SpotEntity> spots, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.code = code;
        this.basePrice = basePrice;
        this.maxCapacity = maxCapacity;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.durationLimitMinutes = durationLimitMinutes;
        this.garage = garage;
        this.spots = spots;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Sector toDomain() {
        return new Sector(
                this.code,
                this.basePrice,
                this.maxCapacity,
                this.openHour,
                this.closeHour,
                this.durationLimitMinutes,
                this.garage.toDomain(),
                this.spots.stream().map(SpotEntity::toDomain).toList()
        );
    }

    public SectorEntity fromDomain(Sector sector) {
        this.code = sector.code();
        this.basePrice = sector.basePrice();
        this.maxCapacity = sector.maxCapacity();
        this.openHour = sector.openHour();
        this.closeHour = sector.closeHour();
        this.durationLimitMinutes = sector.durationLimitMinutes();
        this.garage = new GarageEntity().fromDomain(sector.garage());
        this.spots = sector.spots().stream().map(spot -> {
            SpotEntity spotEntity = new SpotEntity();
            spotEntity.fromDomain(spot);
            spotEntity.setSector(this);
            return spotEntity;
        }).toList();
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public void setSpots(List<SpotEntity> spotEntities) {
        this.spots = spotEntities;
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
