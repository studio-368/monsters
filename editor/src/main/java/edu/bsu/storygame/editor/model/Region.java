package edu.bsu.storygame.editor.model;

import java.util.List;

public class Region {
    public String region;
    public List<Encounter> encounters;

    public Region(String region, List<Encounter> encounters) {
        this.region = region;
        this.encounters = encounters;
    }

    @Override
    public String toString() {
        return region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Region region1 = (Region) o;

        if (region != null ? !region.equals(region1.region) : region1.region != null) return false;
        return encounters != null ? encounters.equals(region1.encounters) : region1.encounters == null;

    }

    @Override
    public int hashCode() {
        int result = region != null ? region.hashCode() : 0;
        result = 31 * result + (encounters != null ? encounters.hashCode() : 0);
        return result;
    }
}
