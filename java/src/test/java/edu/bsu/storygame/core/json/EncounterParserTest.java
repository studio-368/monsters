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
