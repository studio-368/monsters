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

import edu.bsu.storygame.core.model.Encounter;
import edu.bsu.storygame.core.util.EncounterMatchingTestJson;
import org.junit.Test;
import playn.java.JavaPlatform;

import static org.junit.Assert.assertEquals;

public class EncounterParserTest {

    @Test
    public void testParse() throws Exception {
        JavaPlatform.Headless plat = new JavaPlatform.Headless(new JavaPlatform.Config());
        EncounterParser parser = new EncounterParser(plat.json());
        String jsonString = plat.assets().getTextSync("test-encounters/cockatrice.json");
        Encounter encounter = parser.parse(jsonString);
        assertEquals(EncounterMatchingTestJson.create(), encounter);
    }
}
