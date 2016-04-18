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

import com.google.common.collect.ImmutableList;

import java.util.*;

public class StoryDeck implements Iterable<Story> {

    private final ImmutableList<Story> stories;
    private final List<Story> unreadStories = new ArrayList<>();
    private final Random random = new Random();

    public StoryDeck(Collection<Story> stories) {
        this.stories = ImmutableList.copyOf(stories);
        unreadStories.addAll(stories);
    }

    public int size() {
        return stories.size();
    }

    public Story chooseOne() {
        if (unreadStories.size() > 0)
            return unreadStories.remove(0);
        return stories.get(random.nextInt(stories.size()));
    }

    @Override
    public Iterator<Story> iterator() {
        return stories.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoryDeck stories1 = (StoryDeck) o;

        if (!stories.equals(stories1.stories)) return false;
        return unreadStories.equals(stories1.unreadStories);

    }

    @Override
    public int hashCode() {
        int result = stories.hashCode();
        result = 31 * result + unreadStories.hashCode();
        return result;
    }
}
