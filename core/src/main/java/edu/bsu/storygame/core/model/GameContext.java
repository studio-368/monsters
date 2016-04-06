package edu.bsu.storygame.core.model;

import com.google.common.collect.ImmutableList;
import edu.bsu.storygame.core.MonsterGame;
import react.Slot;
import react.Value;

import static com.google.common.base.Preconditions.*;

public final class GameContext {
    public final MonsterGame game;
    public final Value<Phase> phase = Value.create(Phase.MOVEMENT);
    public final ImmutableList<Player> players;
    public final Value<Player> currentPlayer;
    public final Value<Encounter> encounter = Value.create(null);
    public final Value<Reaction> reaction = Value.create(null);
    public final Value<Conclusion> conclusion = Value.create(null);

    public final int pointsRequiredForVictory = 100;

    public GameContext(MonsterGame game, Player... players) {
        this.game = checkNotNull(game);
        checkArgument(players.length > 1, "Must have at least two players");
        for (Player p : players) {
            checkNotNull(p, "Player may not be null");
        }
        this.players = ImmutableList.copyOf(players);
        this.currentPlayer = Value.create(this.players.get(0));

        configureResetEncounterAtEndOfRound();
    }

    private void configureResetEncounterAtEndOfRound() {
        phase.connect(new Slot<Phase>() {
            @Override
            public void onEmit(Phase phase) {
                if (phase == Phase.END_OF_ROUND) {
                    encounter.update(null);
                    reaction.update(null);
                    conclusion.update(null);
                    currentPlayer.update(otherPlayer());
                    GameContext.this.phase.update(Phase.MOVEMENT);
                }
            }
        });
    }

    public final Player otherPlayer() {
        return currentPlayer.get() == players.get(0) ? players.get(1) : players.get(0);
    }

}
