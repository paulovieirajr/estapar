package io.github.paulovieirajr.estapar.adapter.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "garage")
public class GarageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "garage_id", nullable = false, unique = true)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SectorEntity> sectors = new ArrayList<>();

    public GarageEntity() {
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<SectorEntity> getSectors() {
        return Collections.unmodifiableList(sectors);
    }

    public void addSector(SectorEntity sectorEntity) {
        if (sectorEntity == null) {
            throw new IllegalArgumentException("Sector cannot be null");
        }
        sectorEntity.setGarage(this);
        this.sectors.add(sectorEntity);
    }

    public void removeSector(SectorEntity sectorEntity) {
        sectorEntity.setGarage(null);
        this.sectors.remove(sectorEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GarageEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
