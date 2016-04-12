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

package edu.bsu.storygame.core;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ListTest {
    @Test
    public void testTwoEmptyImmutableListsAreEqual() {
        //noinspection EqualsWithItself
        assertTrue(ImmutableList.of().equals(ImmutableList.of()));
    }

    @Test
    public void testTwoEmptyImmutableListsHaveSameHashCode() {
        assertTrue(ImmutableList.of().hashCode() == ImmutableList.of().hashCode());
    }

    @Test
    public void testTwoImmutableListsWithSameContentAreEqual() {
        assertTrue(ImmutableList.of(1).equals(ImmutableList.of(1)));
    }

    @Test
    public void testTwoImmutableListsWithSameContentHaveSameHashcode() {
        assertTrue(ImmutableList.of(1).hashCode() == ImmutableList.of(1).hashCode());
    }
}
