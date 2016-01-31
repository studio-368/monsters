package edu.bsu.storygame.core;

import react.Value;

public final class GameContext {
    public final Value<Phase> phase = Value.create(Phase.MOVEMENT);
}
