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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StoryDeckTest {
    private static final Reaction REACTION = Reaction.create("react").story(
            Story.withText("Story 1").trigger(new NoSkillTrigger(
                    new Conclusion.Builder().text("Conclusion 1").build())).build()
    ).story(
            Story.withText("Story 2").trigger(new NoSkillTrigger(
                    new Conclusion.Builder().text("Conclusion 2").build())).build()
    ).build();

    @Test
    public void testStoriesChosenInOrder() {
        assertEquals(REACTION.stories.chooseOne().text, "Story 1");
        assertEquals(REACTION.stories.chooseOne().text, "Story 2");
    }
}
