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

package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import playn.core.Surface;
import playn.scene.Layer;

import static com.google.common.base.Preconditions.checkArgument;

public class LoadingBar extends Layer {

    private int value;
    private final int max;
    private final float width;
    private final float height;
    private final MonsterGame game;

    public LoadingBar(int max, float width, float height, MonsterGame game) {
        checkArgument(max >= 0);
        this.game = game;
        this.max = max;
        this.width = width;
        this.height = height;
    }

    public void increment() {
        value++;
        if (value > max) {
            game.plat.log().warn("Value (" + value + ") exceeds max (" + max + "); capping.");
            value = max;
        }
    }

    @Override
    public float width() {
        return width;
    }

    @Override
    public float height() {
        return height;
    }

    @Override
    protected void paintImpl(Surface surf) {
        final float percent = value / (float) max;
        surf.setFillColor(Palette.WHITE_SMOKE);
        surf.fillRect(0, 0, width(), height());
        surf.setFillColor(Palette.GOLDEN_POPPY);
        surf.fillRect(0, 0, width(), height() * percent);
    }
}
