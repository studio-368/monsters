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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

public final class Narrative {

    public static final class Builder {
        private final Map<Region, EncounterDeck> map = Maps.newEnumMap(Region.class);

        public Builder() {
        }

        public Builder put(Region region, EncounterDeck deck) {
            this.map.put(region, deck);
            return this;
        }

        public Narrative build() {
            return new Narrative(this);
        }
    }

    private final ImmutableMap<Region, EncounterDeck> map;

    private Narrative(Builder builder) {
        this.map = ImmutableMap.copyOf(builder.map);
    }

    public EncounterDeck forRegion(Region region) {
        if (!map.containsKey(region)) {
            throw new IllegalArgumentException("No deck for region " + region);
        }
        return map.get(region);
    }

    public Set<Skill> skills() {
        Set<Skill> result = Sets.newTreeSet();
        for (EncounterDeck deck : map.values()) {
            for (Encounter encounter : deck) {
                for (Reaction reaction : encounter.reactions) {
                    for (SkillTrigger trigger : reaction.story.triggers) {
                        result.add(trigger.skill);
                    }
                }
            }
        }
        return result;
    }
}
