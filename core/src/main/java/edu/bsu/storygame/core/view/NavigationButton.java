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

package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.Button;

public class NavigationButton extends Button {


    public NavigationButton(String text, final ScreenStack.UIScreen screen) {
        super(text);
        final MonsterGame game = (MonsterGame) screen.game();
        onClick(new Slot<Button>() {
            @Override
            public void onEmit(Button button) {
                game.screenStack.push(screen, game.screenStack.slide());
            }
        });
    }

    @Override
    protected Class<?> getStyleClass() {
        return NavigationButton.class;
    }
}
