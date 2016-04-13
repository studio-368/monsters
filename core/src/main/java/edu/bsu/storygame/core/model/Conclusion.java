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

import java.util.Objects;

public final class Conclusion {

    public static class Builder {
        private String text;
        private int points = 0;
        private Skill skill = null;

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder points(int points) {
            this.points = points;
            return this;
        }

        public Builder skill(Skill skill) {
            this.skill = skill;
            return this;
        }

        public Conclusion build() {
            return new Conclusion(this);
        }
    }

    public final String text;
    public final int points;
    public final Skill skill;

    private Conclusion(Builder importer) {
        this.text = importer.text;
        this.points = importer.points;
        this.skill = importer.skill;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("text", text)
                .add("points", points)
                .add("skill", skill)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Conclusion) {
            Conclusion other = (Conclusion) obj;
            return Objects.equals(this.text, other.text)
                    && Objects.equals(this.points, other.points)
                    && Objects.equals(this.skill, other.skill);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, points, skill);
    }
}
