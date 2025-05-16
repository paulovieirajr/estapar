package io.github.paulovieirajr.estapar.adapter.repository;


import io.github.paulovieirajr.estapar.adapter.repository.entity.SpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<SpotEntity, Integer> {
}
