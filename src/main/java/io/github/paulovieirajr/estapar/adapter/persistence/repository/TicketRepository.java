package io.github.paulovieirajr.estapar.adapter.persistence.repository;

import io.github.paulovieirajr.estapar.adapter.persistence.entity.SpotEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.TicketEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {

    Optional<TicketEntity> findBySpot(SpotEntity spot);

    @Query(nativeQuery = true, value = """
            SELECT t.*
            FROM ticket t
            JOIN vehicle v ON t.vehicle_id = v.id
            WHERE t.valid = :valid
            AND v.license_plate = :licensePlate
            """)
    Optional<TicketEntity> findByValidAndVehicle(Boolean valid, String licensePlate);
}
