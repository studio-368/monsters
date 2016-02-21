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

        Set<String> expected = ImmutableSet.of("Logic", "Magic");
        assertEquals(expected, narrative.skills());
    }
}
