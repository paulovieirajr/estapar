package io.github.paulovieirajr.estapar.service.exception.vehicle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VechicleNotFoundException extends RuntimeException {

    public VechicleNotFoundException(String message) {
        super(message);
    }
}
