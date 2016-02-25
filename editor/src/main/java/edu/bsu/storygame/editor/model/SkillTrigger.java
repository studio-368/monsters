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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkillTrigger that = (SkillTrigger) o;

        if (skill != null ? !skill.equals(that.skill) : that.skill != null) return false;
        return conclusion != null ? conclusion.equals(that.conclusion) : that.conclusion == null;

    }

    @Override
    public int hashCode() {
        int result = skill != null ? skill.hashCode() : 0;
        result = 31 * result + (conclusion != null ? conclusion.hashCode() : 0);
        return result;
    }
}
