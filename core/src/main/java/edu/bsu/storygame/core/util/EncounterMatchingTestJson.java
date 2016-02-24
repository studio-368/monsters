package edu.bsu.storygame.core.util;

import edu.bsu.storygame.core.model.Encounter;
import edu.bsu.storygame.core.model.Reaction;
import edu.bsu.storygame.core.model.SkillTrigger;
import edu.bsu.storygame.core.model.Story;

public class EncounterMatchingTestJson {

    public static Encounter create() {
        return Encounter.with("Cockatrice")
                .image("pic")
                .reaction(Reaction.create("Fight")
                        .story(Story.withText("Story 1")
                                .trigger(SkillTrigger.skill("Logic")
                                        .conclusion("Conclusion 1"))
                                .trigger(SkillTrigger.skill("Magic")
                                        .conclusion("Conclusion 2"))
                                .build()))
                .reaction(Reaction.create("Hide")
                        .story(Story.withText("Story 2")
                                .trigger(SkillTrigger.skill("Logic")
                                        .conclusion("Conclusion 1-A"))
                                .trigger(SkillTrigger.skill("Magic")
                                        .conclusion("Conclusion 2-B"))
                                .build()))
                .build();
    }

}
