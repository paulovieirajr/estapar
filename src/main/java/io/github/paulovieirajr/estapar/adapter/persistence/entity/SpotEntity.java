package io.github.paulovieirajr.estapar.adapter.persistence.entity;

import io.github.paulovieirajr.estapar.domain.model.Spot;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "spot", uniqueConstraints = {
        @UniqueConstraint(name = "uc_lattd_longt", columnNames = {"latitude", "longitude"})
})
public class SpotEntity {

    @Id
    @Column(name = "spot_id", nullable = false)
    private Integer id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "sector_code",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_spot_sector")
    )
    private SectorEntity sector;

    @Column(name = "occupied", nullable = false)
    private boolean occupied;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public SpotEntity() {
    }

    public SpotEntity(Integer id,
                      Double latitude,
                      Double longitude,
                      boolean occupied) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.occupied = occupied;
    }

    public Spot toDomain() {
        return new Spot(
                this.id,
                this.latitude,
                this.longitude,
                this.sector.toDomain(),
                this.occupied
        );
    }

    public SpotEntity fromDomain(Spot spot) {
        this.id = spot.getId();
        this.latitude = spot.getLatitude();
        this.longitude = spot.getLongitude();
        this.sector = new SectorEntity().fromDomain(spot.getSector());
        this.occupied = spot.isOccupied();
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public SectorEntity getSector() {
        return sector;
    }

    public void setSector(SectorEntity sectorEntity) {
        this.sector = sectorEntity;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
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
