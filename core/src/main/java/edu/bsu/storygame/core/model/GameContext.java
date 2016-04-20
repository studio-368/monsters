/*
 * Copyright 2016 Traveler's Notebook: Monster Tales project authors
 *
 * This file is part of monsters
 *
 * monsters is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * monsters is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with monsters.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.bsu.storygame.core.model;

import com.google.common.collect.ImmutableList;
import edu.bsu.storygame.core.MonsterGame;
import react.Slot;
import react.Value;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class GameContext {
    public final MonsterGame game;
    public final Value<Phase> phase = Value.create(Phase.MOVEMENT);
    public final ImmutableList<Player> players;
    public final Value<Player> currentPlayer;
    public final Value<Encounter> encounter = Value.create(null);
    public final Value<Reaction> reaction = Value.create(null);
    public final Value<Story> story = Value.create(null);
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
                    story.update(null);
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
