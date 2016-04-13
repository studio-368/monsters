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

package edu.bsu.storygame.core.json;

import edu.bsu.storygame.core.model.EncounterDeck;
import edu.bsu.storygame.core.model.Narrative;
import edu.bsu.storygame.core.model.Region;
import org.junit.Test;
import playn.java.JavaPlatform;

import static org.junit.Assert.assertTrue;

public class NarrativeIntegrityTest {

    @Test
    public void testEachRegionHasAtLeastOneEncounter() throws Exception {
        JavaPlatform.Headless plat = new JavaPlatform.Headless(new JavaPlatform.Config());
        Narrative narrative = new NarrativeParser(plat.json()).parse(
                plat.assets().getTextSync("encounters/narrative.json"));
        for (Region region : Region.values()) {
            EncounterDeck deck = narrative.forRegion(region);
            assertTrue("Missing encounters for " + region.name(), deck.size() > 0);
        }
    }
}
