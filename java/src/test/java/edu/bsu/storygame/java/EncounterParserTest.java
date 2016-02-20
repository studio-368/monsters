package edu.bsu.storygame.java;

import edu.bsu.storygame.core.model.*;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class EncounterParserTest {

    private final Encounter expected =
            Encounter.with("Cockatrice")
                    .image("pic")
                    .reaction(Reaction.create("Fight")
                            .story(Story.withText("Story 1")
                                    .trigger(SkillTrigger.skill(Skill.LOGIC)
                                            .conclusion("Conclusion 1"))
                                    .trigger(SkillTrigger.skill(Skill.MAGIC)
                                            .conclusion("Conclusion 2"))
                                    .build()))
                    .reaction(Reaction.create("Hide")
                            .story(Story.withText("Story 2")
                                    .trigger(SkillTrigger.skill(Skill.LOGIC)
                                            .conclusion("Conclusion 1-A"))
                                    .trigger(SkillTrigger.skill(Skill.MAGIC)
                                            .conclusion("Conclusion 2-B"))
                                    .build()))
                    .build();

    @Test
    public void testParse() {
        EncounterParser parser = new EncounterParser();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("test-encounters/cockatrice.json");
        Encounter encounter = parser.parse(in);
        assertEquals(expected, encounter);
    }
}
