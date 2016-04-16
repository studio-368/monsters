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
import playn.scene.GroupLayer;
import playn.scene.Layer;
import pythagoras.f.Rectangle;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
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
                makeTPLayer(w, h),
                makeCheckerLayer(w, h),
                Layers.solid(Colors.CYAN, w, h),
                Layers.solid(Colors.BLUE, w, h),
                Layers.solid(Colors.WHITE, w, h));
        layer.add(notebook);

        iface.createRoot(AxisLayout.vertical(), SimpleStyles.newSheet(game.plat.graphics()), layer)
                .setSize(size())
                .add(new Button("Turn page").onClick(new Slot<Button>() {
                            @Override
                            public void onEmit(final Button button) {
                                button.setEnabled(false);
                                notebook.turnPage().onSuccess(new Slot<Notebook>() {
                                    @Override
                                    public void onEmit(Notebook layers) {
                                        button.setEnabled(true);
                                    }
                                });
                            }
                        }),
                        new Button("Close book").onClick(new Slot<Button>() {
                            @Override
                            public void onEmit(Button button) {
                                notebook.closeBook();
                            }
                        }),
                        new Shim(0, 0).setConstraint(AxisLayout.stretched()));
    }

    private Layer makeCheckerLayer(final float w, final float h) {
        GroupLayer product = new GroupLayer();
        product.add(Layers.solid(Colors.LIGHT_GRAY, w, h));
        Layer left = Layers.solid(Colors.YELLOW, w / 2, h);
        Layer right = Layers.solid(Colors.WHITE, w / 2, h);
        product.addAt(left, 0, 0);
        product.addAt(right, w / 2, 0);
        return product;
    }

    private Layer makeTPLayer(final float w, final float h) {
        GroupLayer layer = new GroupLayer();
        iface.createRoot(AxisLayout.vertical(), SimpleStyles.newSheet(game.plat.graphics()), layer)
                .setSize(w, h)
                .addStyles(Style.BACKGROUND.is(Background.solid(Colors.GREEN)))
                .add(new Label("Dedicated to Little Debbie Snackcakes"));
        return layer;
    }

    @Override
    public Game game() {
        return game;
    }
}
