package edu.bsu.storygame.core.model;

public enum Skill {
    WEAPON_USE("Weapon Use"),
    WISDOM("Wisdom");
    public final String text;

    Skill(String text) {
        this.text = text;
    }
}
