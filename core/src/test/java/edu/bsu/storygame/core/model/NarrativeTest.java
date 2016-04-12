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

import com.google.common.collect.ImmutableSet;
import edu.bsu.storygame.core.util.EncounterMatchingTestJson;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class NarrativeTest {

    @Test
    public void testCreate() {
        Narrative narrative =
                new Narrative.Builder().put(Region.AFRICA,
                        EncounterDeck.fromArray(new Encounter[]{
                                EncounterMatchingTestJson.create(),
                                EncounterMatchingTestJson.create()
                        }))
                        .build();
        assertEquals(2, narrative.forRegion(Region.AFRICA).size());
    }

    @Test
    public void testSkills() {
        Narrative narrative =
                new Narrative.Builder().put(Region.AFRICA,
                        EncounterDeck.fromArray(new Encounter[]{
                                EncounterMatchingTestJson.create(),
                                EncounterMatchingTestJson.create()
                        }))
                        .build();

        Set<Skill> expected = ImmutableSet.of(Skill.named("Logic"), Skill.named("Magic"));
        assertEquals(expected, narrative.skills());
    }
}
