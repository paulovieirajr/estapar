package io.github.paulovieirajr.estapar.adapter.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "vehicle_event")
public class VehicleEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "event_type", nullable = false)
    private String eventType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "vehicle_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vehicle_event_vehicle")
    )
    private VehicleEntity vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "spot_id",
            foreignKey = @ForeignKey(name = "fk_vehicle_event_spot")
    )
    private SpotEntity spot;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public VehicleEventEntity() {
    }

    public VehicleEventEntity(String eventType, LocalDateTime eventDate, VehicleEntity vehicle) {
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.vehicle = vehicle;
    }

    public VehicleEventEntity(String eventType, LocalDateTime eventDate, VehicleEntity vehicle, SpotEntity spot) {
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.vehicle = vehicle;
        this.spot = spot;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VehicleEventEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
