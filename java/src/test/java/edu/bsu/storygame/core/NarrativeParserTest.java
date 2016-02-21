package edu.bsu.storygame.core;

import edu.bsu.storygame.core.json.NarrativeParser;
import edu.bsu.storygame.core.model.Narrative;
import edu.bsu.storygame.core.model.Region;
import org.junit.Test;
import playn.java.JavaPlatform;

import static org.junit.Assert.assertEquals;

public class NarrativeParserTest {
    @Test
    public void testParse_africaContainsOneEncounter() throws Exception {
        JavaPlatform.Headless plat = new JavaPlatform.Headless(new JavaPlatform.Config());
        NarrativeParser parser = new NarrativeParser(plat.json());
        String jsonString = plat.assets().getTextSync("test-encounters/narrative.json");
        Narrative narrative = parser.parse(jsonString);
        assertEquals(1, narrative.forRegion(Region.AFRICA).size());
    }
}
