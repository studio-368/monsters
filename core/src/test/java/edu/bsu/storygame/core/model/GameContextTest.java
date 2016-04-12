/*
 * Copyright 2016 Paul Gestwicki
 *
 * This file is part of Traveler's Notebook: Monster Tales
 *
 * Traveler's Notebook: Monster Tales is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Traveler's Notebook: Monster Tales is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Traveler's Notebook: Monster Tales.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.bsu.storygame.core.model;

import com.google.common.collect.Lists;
import edu.bsu.storygame.core.MonsterGame;
import org.junit.Test;
import tripleplay.util.Colors;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public final class GameContextTest {

    @Test
    public void testEncounter_isClearedAtEndOfRound() {
        final List<Skill> skills = Lists.newArrayList();
        GameContext context = new GameContext(mock(MonsterGame.class),
                new Player.Builder().name("Jack")
                        .color(Colors.CYAN).skills(skills).build(),
                new Player.Builder().name("Also Jack")
                        .color(Colors.CYAN).skills(skills).build());
        context.encounter.update(mock(Encounter.class));
        context.phase.update(Phase.END_OF_ROUND);
        assertNull(context.encounter.get());
    }
}
