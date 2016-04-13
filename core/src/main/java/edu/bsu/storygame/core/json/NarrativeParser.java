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
import edu.bsu.storygame.core.model.EncounterDeck;
import edu.bsu.storygame.core.model.Narrative;
import edu.bsu.storygame.core.model.Region;
import edu.bsu.storygame.core.util.ScreamingCapitalizer;
import playn.core.Json;
import playn.core.json.JsonParserException;

import static com.google.common.base.Preconditions.checkNotNull;

public final class NarrativeParser {
    private final Json json;
    private final EncounterParser encounterParser;

    public NarrativeParser(Json json) {
        this.json = checkNotNull(json);
        this.encounterParser = new EncounterParser(json);
    }

    public Narrative parse(String jsonString) throws JsonParserException {
        Narrative.Builder narrativeBuilder = new Narrative.Builder();

        Json.Array array = json.parseArray(jsonString);
        for (int i = 0, limit = array.length(); i < limit; i++) {
            Json.Object obj = array.getObject(i);
            String regionName = obj.getString("region");
            Region region = Region.valueOf(ScreamingCapitalizer.convert(regionName));
            Json.Array encounters = obj.getArray("encounters");
            EncounterDeck deck = parseEncounterDeck(encounters);
            narrativeBuilder.put(region, deck);
        }

        return narrativeBuilder.build();
    }

    private EncounterDeck parseEncounterDeck(Json.Array encounters) {
        EncounterDeck.Builder builder = new EncounterDeck.Builder();
        for (int i = 0, limit = encounters.length(); i < limit; i++) {
            Json.Object obj = encounters.getObject(i);
            Encounter encounter = encounterParser.parse(obj);
            builder.add(encounter);
        }
        return builder.build();
    }
}
