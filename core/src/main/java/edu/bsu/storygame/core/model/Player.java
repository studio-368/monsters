package edu.bsu.storygame.core.model;

import react.RList;
import react.Value;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class Player {
    private final String name;
    private final int color;
    public final RList<String> skills = new RList<>(new ArrayList<String>());
    public final Value<Integer> storyPoints = Value.create(0);
    public final Value<Region> location = Value.create(Region.AFRICA);

    public Player(String name, int color) {
        checkNotNull(name, "Name may not be null");
        checkArgument(!name.trim().isEmpty(), "Name must have non-whitespace characters");
        this.name = name;
        this.color = color;
    }

    public String getName(){return name;}

}
