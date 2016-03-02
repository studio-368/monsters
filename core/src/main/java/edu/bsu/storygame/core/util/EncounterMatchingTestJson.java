package edu.bsu.storygame.core.util;

import edu.bsu.storygame.core.model.*;

public class EncounterMatchingTestJson {

    private static final Skill LOGIC = Skill.named("Logic");
    private static final Skill MAGIC = Skill.named("Magic");

    public static Encounter create() {
        return Encounter.with("Cockatrice")
                .image("pic")
                .reaction(Reaction.create("Fight")
                        .story(Story.withText("Story 1")
                                .trigger(SkillTrigger.skill(LOGIC)
                                        .conclusion(new Conclusion.Builder().text("Conclusion 1").points(1).build()))
                                .trigger(SkillTrigger.skill(MAGIC)
                                        .conclusion(new Conclusion.Builder().text("Conclusion 2").skill(Skill.named("RewardSkill")).build()))
                                .build()))
                .reaction(Reaction.create("Hide")
                        .story(Story.withText("Story 2")
                                .trigger(SkillTrigger.skill(LOGIC)
                                        .conclusion(new Conclusion.Builder().text("Conclusion 1-A").points(1).skill(Skill.named("RewardSkill-2")).build()))
                                .trigger(SkillTrigger.skill(MAGIC)
                                        .conclusion(new Conclusion.Builder().text("Conclusion 2-B").build()))
                                .build()))
                .build();
    }

}
