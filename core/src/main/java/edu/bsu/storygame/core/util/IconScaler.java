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

package edu.bsu.storygame.core.util;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.ImageCache;
import playn.core.Image;
import tripleplay.ui.Icon;
import tripleplay.ui.Icons;

import static com.google.common.base.Preconditions.checkNotNull;

public final class IconScaler {

    private final MonsterGame game;

    public IconScaler(MonsterGame game) {
        this.game = checkNotNull(game);
    }

    public Icon scale(ImageCache.Key key, float desiredWidth) {
        final Image image = game.imageCache.image(key);
        final float scale = desiredWidth / image.width();
        return Icons.scaled(Icons.image(image), scale);
    }
}
