package edu.bsu.storygame.core.model;

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
}
