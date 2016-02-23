package edu.bsu.storygame.editor.json;

import edu.bsu.storygame.core.model.Encounter;
import edu.bsu.storygame.core.model.EncounterDeck;
import edu.bsu.storygame.core.model.Narrative;
import edu.bsu.storygame.core.model.Region;
import edu.bsu.storygame.core.util.ScreamingCapitalizer;
import org.json.JSONArray;
import org.json.JSONObject;

public class NarrativeParser {

    private final EncounterParser encounterParser;

    public NarrativeParser() {
        this.encounterParser = new EncounterParser();
    }

    public Narrative parse(String jsonString) {
        Narrative.Builder narrativeBuilder = new Narrative.Builder();

        JSONArray array = new JSONArray(jsonString);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String regionName = obj.getString("region");
            Region region = Region.valueOf(ScreamingCapitalizer.convert(regionName));
            JSONArray encounters = obj.getJSONArray("encounters");
            EncounterDeck deck = parseEncounterDeck(encounters);
            narrativeBuilder.put(region, deck);
        }
        return narrativeBuilder.build();
    }

    private EncounterDeck parseEncounterDeck(JSONArray encounters) {
        EncounterDeck.Builder builder = new EncounterDeck.Builder();
        for (int i = 0, limit = encounters.length(); i < limit; i++) {
            JSONObject obj = encounters.getJSONObject(i);
            Encounter encounter = encounterParser.parse(obj);
            builder.add(encounter);
        }
        return builder.build();
    }
}
