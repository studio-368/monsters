package edu.bsu.storygame.core.model;

import com.google.common.testing.EqualsTester;
import edu.bsu.storygame.core.PlaceholderEncounterFactory;
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
        return PlaceholderEncounterFactory.createEncounter();
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
