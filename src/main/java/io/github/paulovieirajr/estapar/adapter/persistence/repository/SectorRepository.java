package io.github.paulovieirajr.estapar.adapter.persistence.repository;


import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SectorRepository extends JpaRepository<SectorEntity, String> {
    Optional<SectorEntity> findBySectorCode(String sectorCode);
}
