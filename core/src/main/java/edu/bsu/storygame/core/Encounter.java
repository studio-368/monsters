package edu.bsu.storygame.core;

import com.google.common.collect.ImmutableList;
import playn.core.Image;

public class Encounter {

    public final Image image;
    public final String name = "Angry Cockatrice";
    public final ImmutableList<Reaction> reactions = ImmutableList.of(
            new Reaction("Fight", "You encounter an angry beast and charge forward to fight it like a silly English person.",
                    ImmutableList.of(
                            new SkillTrigger(Skill.WEAPON_USE, "You pound the tar out of that chicken thing."),
                            new SkillTrigger(Skill.WISDOM, "You make peace with the ugly bird lizard."))),
            new Reaction("Hide", "You hear a terrifying sound and hide like a girly man",
                    ImmutableList.of(
                            new SkillTrigger(Skill.WEAPON_USE, "You spend the rest of the day sharpening your sword, not that you have the guts to use it."),
                            new SkillTrigger(Skill.WISDOM, "You relish the mustard of this dinner time."))));

    public Encounter(GameContext context) {
        this.image = context.game.plat.assets().getImage("images/cockatrice.png");
    }
}
