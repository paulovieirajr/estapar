package io.github.paulovieirajr.estapar.startup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.paulovieirajr.estapar.adapter.dto.simulator.EstaparDataSimulatorDto;
import io.github.paulovieirajr.estapar.adapter.dto.simulator.SectorDto;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.GarageRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.SectorRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.repository.SpotRepository;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.GarageEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SectorEntity;
import io.github.paulovieirajr.estapar.adapter.persistence.entity.SpotEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class EstaparGarageSimulatorInitializer implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstaparGarageSimulatorInitializer.class);

    @Value("${estapar.garage.simulator.url}")
    private String estaparGarageSimulatorUrl;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final GarageRepository garageRepository;
    private final SectorRepository sectorRepository;
    private final SpotRepository spotRepository;

    public EstaparGarageSimulatorInitializer(RestClient restClient, ObjectMapper objectMapper,
                                             GarageRepository garageRepository, SectorRepository sectorRepository,
                                             SpotRepository spotRepository) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.garageRepository = garageRepository;
        this.sectorRepository = sectorRepository;
        this.spotRepository = spotRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        execute();
    }

    private void execute() throws JsonProcessingException {
        LOGGER.info("Estapar garage simulator initialized. Recovering data from simulator container...");
        String dataFromContainer = restClient
                .method(HttpMethod.GET)
                .uri(estaparGarageSimulatorUrl)
                .retrieve()
                .body(String.class);

        EstaparDataSimulatorDto data = objectMapper.readValue(dataFromContainer, EstaparDataSimulatorDto.class);

        List<SectorEntity> sectors = recoverSectorEntities(data);
        GarageEntity garageEntity = recoverGarageEntity(sectors);
        List<SpotEntity> spots = recoverSpotEntities(data, sectors);
        saveRecoveredEntities(garageEntity, sectors, spots);
    }

    private static GarageEntity recoverGarageEntity(List<SectorEntity> sectors) {
        LOGGER.info("Recovering garage entity...");
        GarageEntity garageEntity = new GarageEntity();

        for (SectorEntity sector : sectors) {
            garageEntity.addSector(sector);
        }
        return garageEntity;
    }

    private static List<SectorEntity> recoverSectorEntities(EstaparDataSimulatorDto data) {
        LOGGER.info("Recovering sector entities...");
        return data.sectors().stream().map(SectorDto::toEntity).toList();
    }

    private static List<SpotEntity> recoverSpotEntities(EstaparDataSimulatorDto data, List<SectorEntity> sectors) {
        LOGGER.info("Recovering spot entities...");
        List<SpotEntity> spots = data.spots().stream().map(dto -> {
            SpotEntity spot = dto.toEntity();
            SectorEntity sectorEntity = sectors.stream()
                    .filter(sector -> sector.getSectorCode().equals(dto.sectorCode()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Sector not found for spot: " + dto.id()));
            sectorEntity.addSpot(spot);
            return spot;
        }).toList();

        for (SpotEntity spot : spots) {
            SectorEntity sectorEntity = sectors.stream()
                    .filter(sector -> sector.getSectorCode().equals(spot.getSector().getSectorCode()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Sector not found for spot: " + spot.getId()));
            sectorEntity.addSpot(spot);
        }
        return spots;
    }

    private void saveRecoveredEntities(GarageEntity garageEntity, List<SectorEntity> sectors, List<SpotEntity> spots) {
        LOGGER.info("Saving recovered entities...");
        garageRepository.save(garageEntity);
        sectorRepository.saveAll(sectors);
        spotRepository.saveAll(spots);
        LOGGER.info("Recovered entities from Estapar Garage Simulator container has been saved successfully.");
    }
}
