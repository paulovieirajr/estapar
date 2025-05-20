package io.github.paulovieirajr.estapar.adapter.persistence.repository;

import io.github.paulovieirajr.estapar.adapter.persistence.entity.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class VehicleRepositoryTest {

    private static final String LICENSE_PLATE = "ABC1234";

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindVehicleByLicensePlate() {
        String licensePlate = "ABC1234";
        createVehicle();

        VehicleEntity foundVehicle = vehicleRepository.findByLicensePlate(licensePlate).orElse(null);

        assertNotNull(foundVehicle);
        assertEquals(licensePlate, foundVehicle.getLicensePlate());
    }

    private void createVehicle() {
        VehicleEntity vehicle = new VehicleEntity(LICENSE_PLATE);
        entityManager.persist(vehicle);
    }
}