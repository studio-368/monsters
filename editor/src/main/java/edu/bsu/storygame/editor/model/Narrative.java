package edu.bsu.storygame.editor.model;

import java.util.List;

public class Narrative {
    public List<Region> regions;

    public Narrative(List<Region> regions) {
        this.regions = regions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Narrative narrative = (Narrative) o;

        return regions != null ? regions.equals(narrative.regions) : narrative.regions == null;

    }

    @Override
    public int hashCode() {
        return regions != null ? regions.hashCode() : 0;
    }
}
