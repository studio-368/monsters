/*
 * Copyright 2016 Traveler's Notebook: Monster Tales project authors
 *
 * This file is part of monsters
 *
 * monsters is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * monsters is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with monsters.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.bsu.storygame.core.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Story {

    public static Builder withText(String text) {
        return new Builder(text);
    }

    public static final class Builder {
        private final String text;
        private final List<SkillTrigger> triggers = Lists.newArrayList();
        private NoSkillTrigger noSkill;

        private Builder(String text) {
            this.text = text;
        }

        public Builder trigger(SkillTrigger skillTrigger) {
            triggers.add(skillTrigger);
            return this;
        }

        public Builder trigger(NoSkillTrigger noSkill) {
            this.noSkill = checkNotNull(noSkill);
            return this;
        }

        public Story build() {
            return new Story(this);
        }
    }

    public final String text;
    public final ImmutableList<SkillTrigger> triggers;
    public final NoSkillTrigger noSkill;

    private Story(Builder builder) {
        this.text = builder.text;
        this.triggers = ImmutableList.copyOf(builder.triggers);
        this.noSkill = checkNotNull(builder.noSkill, "Every encounter must have a no-skill trigger");
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
