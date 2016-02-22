package edu.bsu.storygame.core;

import edu.bsu.storygame.core.model.*;

public final class PlaceholderEncounterFactory {

    public static Encounter createEncounter() {
        return Encounter.with("Angry Cockatrice")
                .image("cockatrice")
                .reaction(Reaction.create("Fight")
                        .story(Story.withText("You aggressively see a thing")
                                .trigger(SkillTrigger.skill(Skill.WEAPON_USE)
                                        .conclusion("You stab it"))
                                .trigger(SkillTrigger.skill(Skill.LOGIC)
                                        .conclusion("You think it to death"))
                                .build()))
                .reaction(Reaction.create("Hide")
                        .story(Story.withText("When you hear a thing, you hide.")
                                .trigger(SkillTrigger.skill(Skill.WEAPON_USE)
                                        .conclusion("Whatevs"))
                                .build()))
                .build();
    }
}
