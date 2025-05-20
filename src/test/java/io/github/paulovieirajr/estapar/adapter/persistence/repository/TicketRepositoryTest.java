package io.github.paulovieirajr.estapar.adapter.persistence.repository;

import io.github.paulovieirajr.estapar.adapter.persistence.entity.*;
import io.github.paulovieirajr.estapar.adapter.persistence.jpa.JpaTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class TicketRepositoryTest extends JpaTestBase {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should found a ticket by spot")
    void shouldFindBySpot() {
        GarageEntity garage = createGarage();
        SectorEntity sector = createSector(garage);
        SpotEntity spot = createSpot(sector);
        VehicleEntity vehicle = createVehicle();
        VehicleEventEntity vehicleEvent = createVehicleEvent(vehicle);
        persistTicket(vehicle, spot, vehicleEvent);

        Optional<TicketEntity> ticketFound = ticketRepository.findBySpot(spot);

        assertTrue(ticketFound.isPresent());
        assertEquals(ticketFound.get().getSpot(), spot);
    }

    @Test
    @DisplayName("Should found by a valid ticket and vehicle")
    void shouldFindByValidTicketAndVehicle() {
        GarageEntity garage = createGarage();
        SectorEntity sector = createSector(garage);
        SpotEntity spot = createSpot(sector);
        VehicleEntity vehicle = createVehicle();
        VehicleEventEntity vehicleEvent = createVehicleEvent(vehicle);
        persistTicket(vehicle, spot, vehicleEvent);

        Optional<TicketEntity> ticketFound = ticketRepository.findByValidAndVehicle(IS_VALID, vehicle.getLicensePlate());

        assertTrue(ticketFound.isPresent());
        assertTrue(ticketFound.get().isValid());
        assertEquals(ticketFound.get().getVehicle(), vehicle);
    }
}