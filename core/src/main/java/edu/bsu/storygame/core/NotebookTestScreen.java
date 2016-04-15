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

package edu.bsu.storygame.core;

import edu.bsu.storygame.core.view.Notebook;
import playn.core.Game;
import pythagoras.f.Rectangle;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.Button;
import tripleplay.ui.Shim;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;
import tripleplay.util.Layers;

public class NotebookTestScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;

    public NotebookTestScreen(MonsterGame game) {
        super(game.plat);
        this.game = game;
    }

    @Override
    public void wasAdded() {
        float w = 400, h = 300;
        final Notebook notebook = new Notebook(game, iface.anim,
                new Rectangle(game.plat.graphics().viewSize.width() / 2 - w,
                        game.plat.graphics().viewSize.height() / 2 - h / 2,
                        w * 2, h),
                Layers.solid(Colors.YELLOW, w, h),
                Layers.solid(Colors.RED, w, h),
                Layers.solid(Colors.CYAN, w, h),
                Layers.solid(Colors.BLUE, w, h));
        layer.add(notebook);

        iface.createRoot(AxisLayout.vertical(), SimpleStyles.newSheet(game.plat.graphics()), layer)
                .setSize(size())
                .add(new Button("Turn page").onClick(new Slot<Button>() {
                            @Override
                            public void onEmit(Button button) {
                                notebook.turnPage();
                            }
                        }),
                        new Shim(0, 0).setConstraint(AxisLayout.stretched()));
    }

    @Override
    public Game game() {
        return game;
    }
}