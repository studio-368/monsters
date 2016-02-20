package edu.bsu.storygame.core.model;

import static com.google.common.base.Preconditions.checkNotNull;

public enum Skill {
    WEAPON_USE("Weapon Use"),
    ATHLETICISM("Athleticism"),
    LOGIC("Logic"),
    MAGIC("Magic"),
    PERSUASION("Persuasion"),
    STEALTH("Stealth");

    public final String text;

    Skill(String text) {
        this.text = text;
    }

    public static Skill parse(String in) {
        checkNotNull(in);
        for (Skill skill : values()) {
            if (skill.text.equalsIgnoreCase(in)) {
                return skill;
            }
        }
        throw new IllegalArgumentException("No such skill as " + in);
    }
}
