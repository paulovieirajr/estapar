package io.github.paulovieirajr.estapar.adapter.repository;


import io.github.paulovieirajr.estapar.adapter.repository.entity.GarageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GarageRepository extends JpaRepository<GarageEntity, UUID> {
}
