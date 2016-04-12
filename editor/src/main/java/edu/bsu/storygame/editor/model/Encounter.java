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

package edu.bsu.storygame.editor.model;

import java.util.ArrayList;
import java.util.List;

public class Encounter {
    public String name;
    public String image;
    public List<Reaction> reactions;

    public static Encounter emptyEncounter() {
        return new Encounter("", "", new ArrayList<>());
    }

    public Encounter(String name, String image, List<Reaction> reactions) {
        this.name = name;
        this.image = image;
        this.reactions = new ArrayList<>(reactions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Encounter encounter = (Encounter) o;

        if (name != null ? !name.equals(encounter.name) : encounter.name != null) return false;
        if (image != null ? !image.equals(encounter.image) : encounter.image != null) return false;
        return reactions != null ? reactions.equals(encounter.reactions) : encounter.reactions == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (reactions != null ? reactions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
