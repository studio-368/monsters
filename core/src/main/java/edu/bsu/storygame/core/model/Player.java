package edu.bsu.storygame.core.model;

import react.RList;
import react.Value;

import java.util.ArrayList;

public final class Player {

    private final String name;
    private final int color;
    public RList<String> skills = new RList<>(new ArrayList<String>());
    public Value<Integer> storyPoints = Value.create(0);
    public final Value<Region> location = Value.create(Region.AFRICA);

    public static class Builder {

        private String name;
        private int color;
        public RList<String> skills = new RList<>(new ArrayList<String>());

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder skills(RList<String> skills) {
            this.skills = skills;
            return this;
        }

        public Player build() {
            return new Player(this);
        }
    }

    private Player(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.skills = builder.skills;
    }

    public String getName(){return name;}

}

