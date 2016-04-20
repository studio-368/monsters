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

package edu.bsu.storygame.editor.model;

import java.util.ArrayList;
import java.util.List;

public class Reaction {

    public String name;
    public List<Story> stories;

    public static Reaction emptyReaction() {
        return new Reaction("", new ArrayList<>());
    }

    public Reaction(String name, List<Story> stories) {
        this.name = name;
        this.stories = stories;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reaction reaction = (Reaction) o;

        if (name != null ? !name.equals(reaction.name) : reaction.name != null) return false;
        return stories != null ? stories.equals(reaction.stories) : reaction.stories == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (stories != null ? stories.hashCode() : 0);
        return result;
    }
}
