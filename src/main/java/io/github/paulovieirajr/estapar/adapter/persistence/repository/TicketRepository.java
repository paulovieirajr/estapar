package io.github.paulovieirajr.estapar.adapter.persistence.repository;

import io.github.paulovieirajr.estapar.adapter.persistence.entity.SpotEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.TicketEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {

    Optional<TicketEntity> findBySpot(SpotEntity spot);

    Optional<TicketEntity> findByValidAndVehicle(Boolean valid, VehicleEntity vehicle);
}
