package io.github.paulovieirajr.estapar.adapter.persistence.repository;


import io.github.paulovieirajr.estapar.adapter.persistence.entity.VehicleEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleEventRepository extends JpaRepository<VehicleEventEntity, UUID> {
}
