package io.github.paulovieirajr.estapar.service.exception.revenue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RevenueAlreadyExistsException extends RuntimeException {

    public RevenueAlreadyExistsException(String message) {
        super(message);
    }
}
