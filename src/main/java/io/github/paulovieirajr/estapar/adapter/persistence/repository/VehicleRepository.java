package io.github.paulovieirajr.estapar.adapter.persistence.repository;


import io.github.paulovieirajr.estapar.adapter.persistence.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<VehicleEntity, UUID> {
    Optional<VehicleEntity> findByLicensePlate(String licensePlate);
}
