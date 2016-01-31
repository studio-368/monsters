package edu.bsu.storygame.core;

public enum Phase {
    MOVEMENT,
    ENCOUNTER,
    STORY;

    public Phase next() {
        return Phase.values()[(this.ordinal() + 1) % Phase.values().length];
    }
}
