package io.github.paulovieirajr.estapar.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class EstaparGarageSimulatorInitializer implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstaparGarageSimulatorInitializer.class);

    @Value("${estapar.garage.simulator.url}")
    private String estaparGarageSimulatorUrl;

    private final RestClient restClient;

    public EstaparGarageSimulatorInitializer(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        execute();
    }

    private void execute() {
        LOGGER.info("Estapar garage simulator initialized. Recovering data from Simulator...");
        String dataFromContainer = restClient
                .method(HttpMethod.GET)
                .uri(estaparGarageSimulatorUrl)
                .retrieve()
                .body(String.class);
        LOGGER.info("Data from Estapar Garage Simulator: {}", dataFromContainer);
    }
}
