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

package edu.bsu.storygame.core.assets;

import edu.bsu.storygame.core.MonsterGame;
import playn.core.Font;

import static com.google.common.base.Preconditions.checkNotNull;

public enum Typeface {

    TITLE_SCREEN(FontConstants.TITLE_SCREEN_NAME),
    HANDWRITING(FontConstants.HANDWRITING_NAME),
    GAME_TEXT(FontConstants.GAME_TEXT_NAME);

    /**
     * An arbitrary font size.
     * The expectation is that consumers of {@link #font} will use {@link Font#derive(float)}} to make their
     * own sized instance.
     */
    private static final float ARBITRARY_SIZE = 12f;

    public final Font font;

    Typeface(String name) {
        this.font = new Font(name, ARBITRARY_SIZE);
    }

    public FontBuilder in(MonsterGame game) {
        return new FontBuilder(game);
    }

    public final class FontBuilder {
        private final MonsterGame game;
        private FontBuilder(MonsterGame game) {
            this.game = checkNotNull(game);
        }
        public Font atSize(float percentOfHeight) {
            return font.derive(game.bounds.percentOfHeight(percentOfHeight));
        }
    }
}
