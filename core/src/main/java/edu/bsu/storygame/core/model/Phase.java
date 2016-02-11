package edu.bsu.storygame.core.model;

public enum Phase {
    MOVEMENT,
    ENCOUNTER,
    STORY,
    END_OF_ROUND;

    public Phase next() {
        return Phase.values()[(this.ordinal() + 1) % Phase.values().length];
    }
}
