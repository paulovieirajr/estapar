package io.github.paulovieirajr.estapar.service.exception.sector;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SectorAlreadyFullException extends RuntimeException {

    public SectorAlreadyFullException(String message) {
        super(message);
    }
}
