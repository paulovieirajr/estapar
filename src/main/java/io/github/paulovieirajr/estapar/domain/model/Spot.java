package io.github.paulovieirajr.estapar.domain.model;

public final class Spot {

    private final Integer id;
    private final Double latitude;
    private final Double longitude;
    private final Sector sector;
    private boolean occupied;

    public Spot(Integer id, Double latitude, Double longitude, Sector sector, boolean occupied) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sector = sector;
        this.occupied = occupied;
    }

    public Spot(Integer id, Double latitude, Double longitude, Sector sector) {
        this(id, latitude, longitude, sector, false);
    }

    public Integer getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Sector getSector() {
        return sector;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
