package io.github.paulovieirajr.estapar.service.exception.spot;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SpotNotFoundException extends RuntimeException {

    public SpotNotFoundException(String message) {
        super(message);
    }
}
