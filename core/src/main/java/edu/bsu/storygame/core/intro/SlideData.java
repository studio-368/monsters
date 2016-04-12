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

package edu.bsu.storygame.core.intro;

import edu.bsu.storygame.core.assets.ImageCache;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SlideData {

    public static SlideData text(String text) {
        return new SlideData(text);
    }

    public final String text;
    public ImageCache.Key imageKey;
    public String popupText;
    public String nextButtonText;

    private SlideData(String text) {
        this.text = checkNotNull(text);
    }

    public SlideData imageKey(ImageCache.Key key) {
        this.imageKey = checkNotNull(key);
        return this;
    }

    public SlideData popupText(String text) {
        this.popupText = checkNotNull(text);
        return this;
    }

    public SlideData nextButtonText(String text) {
        this.nextButtonText = checkNotNull(text);
        return this;
    }

}
