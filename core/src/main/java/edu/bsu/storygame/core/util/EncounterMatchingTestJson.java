package edu.bsu.storygame.core.util;

import edu.bsu.storygame.core.model.*;

public class EncounterMatchingTestJson {

    public static Encounter create() {
        return Encounter.with("Cockatrice")
                .image("pic")
                .reaction(Reaction.create("Fight")
                        .story(Story.withText("Story 1")
                                .trigger(SkillTrigger.skill("Logic")
                                        .conclusion(new Conclusion.Builder().text("Conclusion 1").points(1).build()))
                                .trigger(SkillTrigger.skill("Magic")
                                        .conclusion(new Conclusion.Builder().text("Conclusion 2").build()))
                                .build()))
                .reaction(Reaction.create("Hide")
                        .story(Story.withText("Story 2")
                                .trigger(SkillTrigger.skill("Logic")
                                        .conclusion(new Conclusion.Builder().text("Conclusion 1-A").points(1).build()))
                                .trigger(SkillTrigger.skill("Magic")
                                        .conclusion(new Conclusion.Builder().text("Conclusion 2-B").build()))
                                .build()))
                .build();
    }

}
