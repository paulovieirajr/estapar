package io.github.paulovieirajr.estapar.adapter.persistence.jpa;

import io.github.paulovieirajr.estapar.adapter.dto.webhook.enums.EventType;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@DataJpaTest
@ActiveProfiles("test")
public class JpaTestBase {

    protected static final String SECTOR_CODE = "A";
    protected static final int MAX_CAPACITY = 10;
    protected static final int DURATION_LIMIT_MINUTES = 240;
    protected static final double LATITUDE = 23.5505;
    protected static final double LONGITUDE = -46.6333;
    protected static final String CURRENCY_CODE = "BRL";
    protected static final LocalDate REVENUE_DATE = LocalDate.of(2024, 5, 1);
    protected static final int REVENUE_VALUE = 500;
    protected static final boolean IS_VALID = true;
    protected static final String LICENSE_PLATE = "ABC1234";

    @Autowired
    protected TestEntityManager entityManager;

    protected GarageEntity createGarage() {
        GarageEntity garage = new GarageEntity();
        entityManager.persist(garage);
        return garage;
    }

    protected SectorEntity createSector(GarageEntity garage) {
        SectorEntity sector = new SectorEntity();
        sector.setSectorCode(SECTOR_CODE);
        sector.setBasePrice(BigDecimal.ONE);
        sector.setMaxCapacity(MAX_CAPACITY);
        sector.setOpenHour(LocalTime.of(8, 0));
        sector.setCloseHour(LocalTime.of(18, 0));
        sector.setDurationLimitMinutes(DURATION_LIMIT_MINUTES);
        sector.setGarage(garage);
        entityManager.persist(sector);
        return sector;
    }

    protected void persistRevenue(SectorEntity sector) {
        RevenueEntity revenue = new RevenueEntity();
        revenue.setDate(REVENUE_DATE);
        revenue.setAmount(BigDecimal.valueOf(REVENUE_VALUE));
        revenue.setCurrencyCode(CURRENCY_CODE);
        revenue.setSector(sector);
        entityManager.persist(revenue);
    }

    protected void persistSector(GarageEntity garage) {
        SectorEntity sector = createSector(garage);
        entityManager.persist(sector);
    }

    protected void persistSpot(SectorEntity sector) {
        SpotEntity spot = new SpotEntity(1, LATITUDE, LONGITUDE, false);
        spot.setSector(sector);
        entityManager.persist(spot);
    }

    protected void persistTicket(VehicleEntity vehicle, SpotEntity spot, VehicleEventEntity vehicleEvent) {
        TicketEntity ticket = new TicketEntity(vehicle);
        ticket.setSpot(spot);
        ticket.setValid(IS_VALID);
        ticket.setEntryTime(vehicleEvent.getEventDate());
        entityManager.persist(ticket);
    }

    protected VehicleEventEntity createVehicleEvent(VehicleEntity vehicle) {
        VehicleEventEntity vehicleEvent = new VehicleEventEntity(EventType.ENTRY.getValue(), LocalDateTime.now(), vehicle);
        entityManager.persist(vehicleEvent);
        return vehicleEvent;
    }

    protected VehicleEntity createVehicle() {
        VehicleEntity vehicle = new VehicleEntity(LICENSE_PLATE);
        entityManager.persist(vehicle);
        return vehicle;
    }

    protected SpotEntity createSpot(SectorEntity sector) {
        SpotEntity spot = new SpotEntity(1, LATITUDE, LONGITUDE, false);
        spot.setSector(sector);
        entityManager.persist(spot);
        return spot;
    }
}
