package io.github.paulovieirajr.estapar.adapter.persistence.repository;

import io.github.paulovieirajr.estapar.adapter.persistence.entity.GarageEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.jpa.JpaTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SectorRepositoryTest extends JpaTestBase {

    @Autowired
    private SectorRepository sectorRepository;

    @Test
    @DisplayName("Should found a sector by code")
    void shouldFoundByCode() {
        GarageEntity garage = createGarage();
        persistSector(garage);

        Optional<SectorEntity> sectorFound = sectorRepository.findBySectorCode(SECTOR_CODE);
        assertThat(sectorFound.isPresent()).isTrue();
        assertThat(sectorFound.get().getSectorCode()).isEqualTo(SECTOR_CODE);
    }
}