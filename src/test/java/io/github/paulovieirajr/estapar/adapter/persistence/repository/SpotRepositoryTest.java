package io.github.paulovieirajr.estapar.adapter.persistence.repository;

import io.github.paulovieirajr.estapar.adapter.persistence.entity.GarageEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SpotEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.jpa.JpaTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DataJpaTest
@ActiveProfiles("test")
class SpotRepositoryTest extends JpaTestBase {

    @Autowired
    private SpotRepository spotRepository;

    @Test
    @DisplayName("Should found a spot by latitude and longitude")
    void shouldFindSpotByLatitudeAndLongitude() {
        GarageEntity garage = createGarage();
        SectorEntity sector = createSector(garage);
        persistSpot(sector);

        Optional<SpotEntity> spotFound = spotRepository.findByLatitudeAndLongitude(LATITUDE, LONGITUDE);

        spotFound.ifPresentOrElse(spot -> {
                    assertEquals(LATITUDE, spot.getLatitude());
                    assertEquals(LONGITUDE, spot.getLongitude());
                },
                () -> fail("Spot not found")
        );
    }
}