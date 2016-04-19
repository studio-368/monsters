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

import com.google.common.collect.Lists;
import com.google.common.testing.EqualsTester;
import org.junit.Test;
import tripleplay.util.Colors;

import java.util.List;

public class PlayerTest {

    @Test
    public void testEquals() {
        final String p1Name = "Player 1";
        final String p2Name = "Player 2";
        final List<Skill> skills = Lists.newArrayList();
        Player p1 = new Player.Builder().name(p1Name)
                .color(Colors.CYAN).skills(skills).build();
        Player p2 = new Player.Builder().name(p2Name)
                .color(Colors.CYAN).skills(skills).build();
        new EqualsTester()
                .addEqualityGroup(p1)
                .addEqualityGroup(p2)
                .testEquals();
    }
}
