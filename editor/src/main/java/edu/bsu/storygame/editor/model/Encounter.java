package edu.bsu.storygame.editor.model;

import java.util.ArrayList;
import java.util.List;

public class Encounter {
    public String name;
    public String image;
    public List<Reaction> reactions;

    public static Encounter emptyEncounter() {
        return new Encounter("", "", new ArrayList<>());
    }

    public Encounter(String name, String image, List<Reaction> reactions) {
        this.name = name;
        this.image = image;
        this.reactions = new ArrayList<>(reactions);
    }

    @Override
    public String toString() {
        return name;
    }
}
