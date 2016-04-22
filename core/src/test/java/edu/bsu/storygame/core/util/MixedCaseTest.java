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

package edu.bsu.storygame.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MixedCaseTest {

    @Test
    public void testConvert_hello() {
        assertEquals("Hello", MixedCase.convert("hello"));
    }

    @Test
    public void testConvert_twoWords_spaceSeparation() {
        assertEquals("Two Words", MixedCase.convert("two words"));
    }

    @Test
    public void testConvert_twoWords_underscoreSeparation() {
        assertEquals("Two Words", MixedCase.convert("TWO_WORDS"));
    }
}
