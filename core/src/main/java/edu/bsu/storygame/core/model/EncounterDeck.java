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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class EncounterDeck implements Iterable<Encounter> {

    public static final class Builder {
        private List<Encounter> encounters = Lists.newArrayList();

        public Builder add(Encounter... encounters) {
            this.encounters.addAll(Arrays.asList(encounters));
            return this;
        }

        public EncounterDeck build() {
            return new EncounterDeck(encounters);
        }
    }

    public static EncounterDeck fromArray(Encounter[] encounters) {
        return new EncounterDeck(Arrays.asList(encounters));
    }

    private final ImmutableList<Encounter> encounters;

    private EncounterDeck(Collection<Encounter> encounters) {
        this.encounters = ImmutableList.copyOf(encounters);
    }

    public int size() {
        return encounters.size();
    }

    public Encounter chooseOne() {
        return encounters.get((int) (encounters.size() * Math.random()));
    }

    @Override
    public Iterator<Encounter> iterator() {
        return encounters.iterator();
    }

}
