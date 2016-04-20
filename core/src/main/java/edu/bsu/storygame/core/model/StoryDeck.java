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

package edu.bsu.storygame.core.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.bsu.storygame.core.util.Shuffler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.*;

public class StoryDeck implements Iterable<Story> {

    private final ImmutableList<Story> stories;
    private final List<Story> unreadStories = new ArrayList<>();

    public StoryDeck(Collection<Story> stories) {
        checkArgument(!stories.isEmpty());
        List<Story> shufflableList = new ArrayList<>(stories);
        Shuffler.shuffle(shufflableList);
        this.stories = ImmutableList.copyOf(shufflableList);
        unreadStories.addAll(this.stories);
    }

    public int size() {
        return stories.size();
    }

    public Story chooseOne() {
        if (!unreadStories.isEmpty()) {
            return unreadStories.remove(0);
        } else {
            unreadStories.addAll(this.stories);
            return chooseOne();
        }
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
        return com.google.common.base.Objects.equal(stories, stories1.stories);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(stories);
    }
}
