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
}
