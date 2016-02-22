package edu.bsu.storygame.core;

import edu.bsu.storygame.core.model.Encounter;
import edu.bsu.storygame.core.model.Reaction;
import edu.bsu.storygame.core.model.SkillTrigger;
import edu.bsu.storygame.core.model.Story;

public final class PlaceholderEncounterFactory {

    public static Encounter createEncounter() {
        return Encounter.with("Angry Cockatrice")
                .image("cockatrice")
                .reaction(Reaction.create("Fight")
                        .story(Story.withText("You aggressively see a thing")
                                .trigger(SkillTrigger.skill("Weapon Use")
                                        .conclusion("You stab it"))
                                .trigger(SkillTrigger.skill("Logic")
                                        .conclusion("You think it to death"))
                                .build()))
                .reaction(Reaction.create("Hide")
                        .story(Story.withText("When you hear a thing, you hide.")
                                .trigger(SkillTrigger.skill("Weapon Use")
                                        .conclusion("Whatevs"))
                                .build()))
                .build();
    }
}
