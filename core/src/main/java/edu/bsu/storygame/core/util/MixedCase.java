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

public final class MixedCase {
    public static String convert(String s) {
        StringBuilder builder = new StringBuilder();
        boolean capitalizeNext = true;
        for (int i = 0, limit = s.length(); i < limit; i++) {
            char character = s.charAt(i);
            if (isCapitalizationTrigger(character)) {
                builder.append(' ');
                capitalizeNext = true;
            } else {
                builder.append(capitalizeNext ? Character.toUpperCase(character) : Character.toLowerCase(character));
                capitalizeNext = false;
            }
        }
        return builder.toString();
    }

    private static boolean isCapitalizationTrigger(char character) {
        return character == ' ' || character == '_';
    }
}
