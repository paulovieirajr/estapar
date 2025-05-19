package io.github.paulovieirajr.estapar.service.exception.sector;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SectorNotFoundException extends RuntimeException {

    public SectorNotFoundException(String message) {
        super(message);
    }
}
