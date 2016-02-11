package edu.bsu.storygame.core.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class Player {
    public final String name;
    public Player(String name) {
        checkNotNull(name, "Name may not be null");
        checkArgument(!name.trim().isEmpty(), "Name must have non-whitespace characters");
        this.name = name;
    }
}
