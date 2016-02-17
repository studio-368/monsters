package edu.bsu.storygame.core.model;

import react.RList;
import react.Value;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class Player {
    private final String playerName;
    private final int playerColor;
    public final RList<Skill>  playerSkills = new RList<>(new ArrayList<Skill>());
    public final Value<Integer> playerPoints = Value.create(0);
    private Region playerRegion = Region.AMERICAS;

    public Player(String name, int color) {
        checkNotNull(name, "Name may not be null");
        checkArgument(!name.trim().isEmpty(), "Name must have non-whitespace characters");
        this.playerName = name;
        this.playerColor = color;
    }

    public void setRegion(Region region){playerRegion = region;}

    public Region getPlayerRegion(){return  playerRegion;}

    public String getPlayerName(){return playerName;}

    public int getPlayerColor() {return  playerColor;}
}
