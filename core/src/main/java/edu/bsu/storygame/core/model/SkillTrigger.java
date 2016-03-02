package edu.bsu.storygame.core.model;

import com.google.common.base.MoreObjects;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SkillTrigger {

    public static Builder skill(Skill skill) {
        return new Builder(skill);
    }

    public static final class Builder {
        private final Skill skill;
        private Conclusion conclusion;

        private Builder(Skill skill) {
            this.skill = checkNotNull(skill);
        }

        public SkillTrigger conclusion(Conclusion conclusion) {
            this.conclusion = checkNotNull(conclusion);
            return new SkillTrigger(this);
        }
    }

    public final Skill skill;
    public final Conclusion conclusion;

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
