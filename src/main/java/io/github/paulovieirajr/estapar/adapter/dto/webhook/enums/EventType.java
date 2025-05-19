package io.github.paulovieirajr.estapar.adapter.dto.webhook.enums;

public enum EventType {
    ENTRY("ENTRY"),
    PARKED("PARKED"),
    EXIT("EXIT");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
