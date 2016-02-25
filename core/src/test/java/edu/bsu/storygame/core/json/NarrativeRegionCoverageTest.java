package edu.bsu.storygame.core.json;

import edu.bsu.storygame.core.model.EncounterDeck;
import edu.bsu.storygame.core.model.Narrative;
import edu.bsu.storygame.core.model.Region;
import org.junit.Test;
import playn.java.JavaPlatform;

import static org.junit.Assert.assertTrue;

public class NarrativeRegionCoverageTest {

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
