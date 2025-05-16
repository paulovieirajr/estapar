package io.github.paulovieirajr.estapar.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record Garage(
        UUID id,
        List<Sector> sectors
) {

    public Garage(UUID id, List<Sector> sectors) {
        if (id == null || sectors == null) {
            throw new IllegalArgumentException("id and sectors cannot be null");
        }
        this.id = id;
        this.sectors = new ArrayList<>(sectors);
    }

    @Override
    public List<Sector> sectors() {
        return Collections.unmodifiableList(sectors);
    }

    public void addSector(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException("Sector cannot be null");
        }
        this.sectors.add(sector);
    }

    public void removeSector(Sector sector) {
        this.sectors.remove(sector);
    }
}
