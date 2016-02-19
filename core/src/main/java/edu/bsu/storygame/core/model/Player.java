package edu.bsu.storygame.core.model;

import react.RList;
import react.Value;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class Player {
    private final String name;
    private final int color;
    public final RList<Skill> skills = new RList<>(new ArrayList<Skill>());
    public final Value<Integer> points = Value.create(0);
    private Region region = Region.AMERICAS;

    public Player(String name, int color) {
        checkNotNull(name, "Name may not be null");
        checkArgument(!name.trim().isEmpty(), "Name must have non-whitespace characters");
        this.name = name;
        this.color = color;
    }

    public void setRegion(Region region){
        this.region = region;}

    public Region getRegion(){return region;}

    public String getName(){return name;}

    public int getColor() {return color;}
}
