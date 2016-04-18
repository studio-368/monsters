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

package edu.bsu.storygame.html;

import playn.html.HtmlPlatform;
import tripleplay.platform.NativeTextField;
import tripleplay.platform.TPPlatform;
import tripleplay.ui.Field;

public class HtmlTpPlatform extends TPPlatform {

    private final HtmlPlatform plat;

    public HtmlTpPlatform(HtmlPlatform plat) {
        this.plat = plat;
    }

    @Override
    public boolean hasNativeTextFields() {
        return true;
    }

    @Override
    public NativeTextField refresh(NativeTextField previous) {
        // TODO Check for style changes
        return previous;
    }

    @Override
    public NativeTextField createNativeTextField(Field.Native field) {
        return new HtmlNativeTextField(field, plat);
    }
}
