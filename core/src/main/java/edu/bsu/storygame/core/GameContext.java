package edu.bsu.storygame.core;

import react.Value;

import static com.google.common.base.Preconditions.checkNotNull;

public final class GameContext {
    public final MonsterGame game;
    public final Value<Phase> phase = Value.create(Phase.MOVEMENT);

    public GameContext(MonsterGame game) {
        this.game = checkNotNull(game);
    }
}
