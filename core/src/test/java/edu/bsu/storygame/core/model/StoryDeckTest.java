/*
 * Copyright 2016 Traveler's Notebook: Monster Tales project authors
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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StoryDeckTest {

    private static final ImmutableList<Story> STORY_LIST = ImmutableList.of(
            Story.withText("Story 1").trigger(new NoSkillTrigger(
                    new Conclusion.Builder().text("Conclusion 1").build())).build(),
            Story.withText("Story 2").trigger(new NoSkillTrigger(
                    new Conclusion.Builder().text("Conclusion 2").build())).build());
    private static final int ITERATIONS_FOR_STOCHASTIC_TEST = 50;

    private StoryDeck deck;

    @Test
    public void testChooseOne_initialOrderIsRandom() {
        initializeDeck();
        Story firstStoryFirstTime = deck.chooseOne();
        for (int i = 0; i < ITERATIONS_FOR_STOCHASTIC_TEST; i++) {
            initializeDeck();
            if (!deck.chooseOne().equals(firstStoryFirstTime)) {
                return;
            }
        }
        fail("The order of stories never changed.");
    }

    private void initializeDeck() {
        deck = new StoryDeck(STORY_LIST);
    }

    @Test
    public void testChooseOne_canStillSeeAllStoriesAfterExhaustingDeck() {
        initializeDeck();
        for (int i = 0; i < STORY_LIST.size(); i++) {
            deck.chooseOne();
        }
        List<Story> secondPassStories = Lists.newArrayList();
        for (int i = 0; i < STORY_LIST.size(); i++) {
            secondPassStories.add(deck.chooseOne());
        }
        assertTrue("Not all stories were encountered in the second pass through the deck",
                secondPassStories.containsAll(STORY_LIST));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_mustHaveAtLeastOneStory() {
        new StoryDeck(new ArrayList<Story>());
    }
}
