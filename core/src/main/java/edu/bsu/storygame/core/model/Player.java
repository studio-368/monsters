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

import com.google.common.collect.Lists;
import react.RList;
import react.Value;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

public class Player {

    public final String name;
    public final int color;
    public RList<Skill> skills;
    public Value<Integer> storyPoints = Value.create(0);
    public Value<Region> location;

    public static class Builder {

        private String name;
        private int color;
        private List<Skill> skills;
        private Region location;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder skills(List<Skill> skills) {
            checkState(this.skills == null, "Skills already specified");
            this.skills = Lists.newArrayList(skills);
            Collections.sort(this.skills);
            return this;
        }

        public Builder location(Region region) {
            this.location = region;
            return this;
        }

        public Player build() {
            return new Player(this);
        }
    }

    private Player(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.skills = RList.create(builder.skills);
        this.location = Value.create(builder.location);
    }

}

