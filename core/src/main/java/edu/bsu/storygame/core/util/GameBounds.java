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

package edu.bsu.storygame.core.util;

import com.google.common.base.MoreObjects;
import pythagoras.f.IRectangle;
import pythagoras.f.Rectangle;

public final class GameBounds {

    private final IRectangle bounds;

    public GameBounds(IRectangle box) {
        this.bounds = new Rectangle(box);
    }

    public float percentOfHeight(float percent) {
        return bounds.height() * percent;
    }

    public float x() {
        return bounds.x();
    }

    public float y() {
        return bounds.y();
    }

    public float width() {
        return bounds.width();
    }

    public float height() {
        return bounds.height();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bounds", bounds)
                .toString();
    }
}
