package edu.bsu.storygame.core.model;

import com.google.common.collect.ImmutableList;
import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.model.Phase;
import edu.bsu.storygame.core.model.Player;
import react.Value;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class GameContext {
    public final MonsterGame game;
    public final Value<Phase> phase = Value.create(Phase.MOVEMENT);
    public final ImmutableList<Player> players;
    public final Value<Player> currentPlayer;

    public GameContext(MonsterGame game, Player... players) {
        this.game = checkNotNull(game);
        checkArgument(players.length > 1, "Must have at least two players");
        for (Player p : players) {
            checkNotNull(p, "Player may not be null");
        }
        this.players = ImmutableList.copyOf(players);
        this.currentPlayer = Value.create(players[0]);
    }
}
