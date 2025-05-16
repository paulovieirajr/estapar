package io.github.paulovieirajr.estapar.domain.model;

public record Spot(
        Integer id,
        Double latitude,
        Double longitude,
        Sector sector,
        boolean occupied
) {
    public Spot(Integer id, Double latitude, Double longitude, Sector sector) {
        this(id, latitude, longitude, sector, false);
    }
}
