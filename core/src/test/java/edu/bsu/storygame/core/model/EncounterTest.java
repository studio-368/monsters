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

import com.google.common.testing.EqualsTester;
import edu.bsu.storygame.core.util.EncounterMatchingTestJson;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EncounterTest {

    private Encounter encounter;

    @Test
    public void testCreate() {
        givenASampleEncounter();
        assertEquals(2, encounter.reactions.size());
    }

    @Test
    public void testCreate_firstReaction() {
        givenASampleEncounter();
        assertEquals("Fight", encounter.reactions.get(0).name);
    }

    @Test
    public void testCreate_secondReaction() {
        givenASampleEncounter();
        assertEquals("Hide", encounter.reactions.get(1).name);
    }

    private void givenASampleEncounter() {
        encounter = makeSampleEncounter();
    }

    private Encounter makeSampleEncounter() {
        return EncounterMatchingTestJson.create();
    }

    @Test
    public void testEquals() {
        Encounter e1 = makeSampleEncounter();
        Encounter e2 = makeSampleEncounter();
        Encounter e3 = Encounter.with("something else").image("foo").build();
        new EqualsTester().addEqualityGroup(e1, e2)
                .addEqualityGroup(e3)
                .testEquals();
    }
}
