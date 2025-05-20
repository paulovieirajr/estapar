package io.github.paulovieirajr.estapar.adapter.persistence.repository;

import io.github.paulovieirajr.estapar.adapter.persistence.entity.RevenueEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface RevenueRepository extends JpaRepository<RevenueEntity, UUID> {
    Optional<RevenueEntity> findByDateAndSector(LocalDate date, SectorEntity sector);
}
