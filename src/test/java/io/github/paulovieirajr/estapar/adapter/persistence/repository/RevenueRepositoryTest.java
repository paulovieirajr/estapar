package io.github.paulovieirajr.estapar.adapter.persistence.repository;

import io.github.paulovieirajr.estapar.adapter.persistence.entity.GarageEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.RevenueEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.jpa.JpaTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class RevenueRepositoryTest extends JpaTestBase {

    @Autowired
    private RevenueRepository revenueRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should find revenue by date and sector")
    public void testFindByDateAndSector() {
        GarageEntity garage = createGarage();
        SectorEntity sector = createSector(garage);
        persistRevenue(sector);

        Optional<RevenueEntity> found = revenueRepository.findByDateAndSector(REVENUE_DATE, sector);
        assertThat(found).isPresent();
        assertThat(found.get().getAmount()).isEqualTo(BigDecimal.valueOf(REVENUE_VALUE));
    }
}