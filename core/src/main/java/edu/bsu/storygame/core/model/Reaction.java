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

public final class Reaction {

    public static Builder create(String name) {
        return new Builder(name);
    }

    public static final class Builder {
        private final String name;
        private Story story;

        private Builder(String name) {
            this.name = checkNotNull(name);
        }

        public Reaction story(Story story) {
            this.story = checkNotNull(story);
            return new Reaction(this);
        }
    }

    public final String name;
    public final Story story;

    private Reaction(Builder builder) {
        this.name = builder.name;
        this.story = builder.story;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("story", story)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Reaction) {
            Reaction other = (Reaction) obj;
            return Objects.equals(this.name, other.name)
                    && Objects.equals(this.story, other.story);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, story);
    }
}
