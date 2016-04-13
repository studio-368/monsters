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

package edu.bsu.storygame.core.view;

import playn.core.Color;

// We keep all of the palette color declarations even if they are unused,
// so that this file matches the palette in the design log.
@SuppressWarnings("unused")
public final class Palette {

    public static final int ROSE = Color.rgb(218, 168, 155);
    public static final int OBSERVATORY = Color.rgb(0, 136, 104);
    public static final int WELL_READ = Color.rgb(140, 53, 65);
    public static final int GOLDEN_POPPY = Color.rgb(240, 197, 0);
    public static final int BLACKCURRANT = Color.rgb(44, 15, 55);
    public static final int NOBEL = Color.rgb(153, 153, 153);
    public static final int BROWN_POD = Color.rgb(66, 33, 11);
    public static final int BLACK = Color.rgb(0, 0, 0);
    public static final int WHITE_SMOKE = Color.rgb(242, 242, 242);
    public static final int SWIRL = Color.rgb(216, 201, 188);

    public static final int PLAYER_ONE = WELL_READ;
    public static final int PLAYER_TWO = BROWN_POD;

    private Palette() {
    }
}
