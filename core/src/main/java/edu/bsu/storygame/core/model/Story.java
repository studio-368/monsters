package edu.bsu.storygame.core.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

public final class Story {

    public static Builder withText(String text) {
        return new Builder(text);
    }

    public static final class Builder {
        private final String text;
        private final List<SkillTrigger> triggers = Lists.newArrayList();

        private Builder(String text) {
            this.text = text;
        }

        public Builder trigger(SkillTrigger skillTrigger) {
            triggers.add(skillTrigger);
            return this;
        }

        public Story build() {
            return new Story(this);
        }
    }

    public final String text;
    public final ImmutableList<SkillTrigger> triggers;

    private Story(Builder builder) {
        this.text = builder.text;
        this.triggers = ImmutableList.copyOf(builder.triggers);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("text", text)
                .add("triggers", triggers)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, triggers);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Story) {
            Story other = (Story) obj;
            return Objects.equals(this.text, other.text)
                    && Objects.equals(this.triggers, other.triggers);
        } else {
            return false;
        }
    }
}
