package edu.bsu.storygame.editor.model;

public class SkillTrigger {
    public String skill;
    public String conclusion;

    public SkillTrigger(String skill, String conclusion) {
        this.skill = skill;
        this.conclusion = conclusion;
    }

    @Override
    public String toString() {
        return skill;
    }
}
