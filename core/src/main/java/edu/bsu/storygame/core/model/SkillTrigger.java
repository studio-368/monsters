package edu.bsu.storygame.core.model;

import com.google.common.base.MoreObjects;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SkillTrigger {

    public static Builder skill(String skill) {
        return new Builder(skill);
    }

    public static final class Builder {
        private final String skill;
        private String conclusion;

        private Builder(String skill) {
            this.skill = checkNotNull(skill);
        }

        public SkillTrigger conclusion(String conclusion) {
            this.conclusion = checkNotNull(conclusion);
            return new SkillTrigger(this);
        }
    }

    public final String skill;
    public final String conclusion;

    private SkillTrigger(Builder builder) {
        this.skill = builder.skill;
        this.conclusion = builder.conclusion;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("skill", skill)
                .add("conclusion", conclusion)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof SkillTrigger) {
            SkillTrigger other = (SkillTrigger) obj;
            return Objects.equals(this.skill, other.skill)
                    && Objects.equals(this.conclusion, other.conclusion);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(skill, conclusion);
    }
}
