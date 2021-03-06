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

package edu.bsu.storygame.core.assets;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.json.NarrativeParser;
import edu.bsu.storygame.core.model.Narrative;
import playn.core.json.JsonParserException;
import react.RFuture;
import react.RPromise;
import react.Slot;

public abstract class NarrativeCache {
    private static final String NARRATIVE = "encounters/narrative.json";
    public final RFuture<Narrative> state = RPromise.create();

    public static final class Default extends NarrativeCache {
        public Default(final MonsterGame game) {
            game.plat.assets().getText(NARRATIVE).onSuccess(new Slot<String>() {
                @Override
                public void onEmit(String jsonString) {
                    final NarrativeParser narrativeParser = new NarrativeParser(game.plat.json());
                    try {
                        Narrative narrative = narrativeParser.parse(jsonString);
                        ((RPromise<Narrative>) state).succeed(narrative);
                    } catch (JsonParserException parseException) {
                        ((RPromise<Narrative>) state).fail(parseException);
                    }
                }
            }).onFailure(new Slot<Throwable>() {
                @Override
                public void onEmit(Throwable throwable) {
                    game.plat.log().warn("Cannot parse the narrative:" + throwable.getMessage());
                    throw new IllegalStateException(throwable);
                }
            });
        }
    }

    public static final class Overridden extends NarrativeCache {
        public Overridden(Narrative narrative) {
            ((RPromise<Narrative>) state).succeed(narrative);
        }
    }


}
