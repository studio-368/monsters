/*
 * Copyright 2016 Traveler's Notebook: Monster Tales project authors
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

package edu.bsu.storygame.core.assets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

public class AudioRandomizer {
    public enum Event {
        PAGE_FLIP(AudioCache.Key.PAGE_FLIP_1, AudioCache.Key.PAGE_FLIP_2, AudioCache.Key.PAGE_FLIP_3),
        HANDOFF_SLIDE(AudioCache.Key.HANDOFF_SLIDE_1, AudioCache.Key.HANDOFF_SLIDE_2, AudioCache.Key.HANDOFF_SLIDE_3, AudioCache.Key.HANDOFF_SLIDE_4),
        TRAVEL(AudioCache.Key.TRAVEL_1, AudioCache.Key.TRAVEL_2);

        private final ImmutableList<AudioCache.Key> keys;

        Event(AudioCache.Key... keys) {
            this.keys = ImmutableList.copyOf(keys);
        }
    }

    private final Random random = new Random();

    private final EnumMap<Event, List<AudioCache.Key>> shuffledEvents = Maps.newEnumMap(Event.class);

    public AudioRandomizer() {
        for (Event event : Event.values()) {
            shuffledEvents.put(event, shuffleSounds(event));
        }
    }

    private List<AudioCache.Key> shuffleSounds(Event event) {
        List<AudioCache.Key> eventSounds = new ArrayList<>(event.keys);
        List<AudioCache.Key> shuffledSounds = new ArrayList<>();
        while (eventSounds.size() > 0) {
            int i = random.nextInt(eventSounds.size());
            shuffledSounds.add(eventSounds.remove(i));
        }
        return shuffledSounds;
    }

    public AudioCache.Key getKey(Event event) {
        if (shuffledEvents.get(event).size() == 0)
            shuffledEvents.put(event, shuffleSounds(event));
        return shuffledEvents.get(event).remove(0);
    }
}
