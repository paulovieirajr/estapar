package io.github.paulovieirajr.estapar.service.exception.spot;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SpotAlreadyOccupiedException extends RuntimeException {

    public SpotAlreadyOccupiedException(String message) {
        super(message);
    }
}
