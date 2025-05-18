package io.github.paulovieirajr.estapar.adapter.persistence.repository;


import io.github.paulovieirajr.estapar.adapter.persistence.entity.SpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<SpotEntity, Integer> {
}
