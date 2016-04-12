/*
 * Copyright 2016 Paul Gestwicki
 *
 * This file is part of Traveler's Notebook: Monster Tales
 *
 * Traveler's Notebook: Monster Tales is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Traveler's Notebook: Monster Tales is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Traveler's Notebook: Monster Tales.  If not, see <http://www.gnu.org/licenses/>.
 */

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
