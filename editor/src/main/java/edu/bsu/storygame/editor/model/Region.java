package edu.bsu.storygame.editor.model;

import java.util.List;

public class Region {
    public String name;
    public List<Encounter> encounters;

    public Region(String name, List<Encounter> encounters) {
        this.name = name;
        this.encounters = encounters;
    }
}
