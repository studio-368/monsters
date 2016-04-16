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

import com.google.common.collect.ImmutableList;
import edu.bsu.storygame.core.model.Encounter;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import edu.bsu.storygame.core.model.Skill;
import edu.bsu.storygame.core.view.notebook.Notebook;
import edu.bsu.storygame.core.view.notebook.PageLayerFactory;
import playn.core.Game;
import pythagoras.f.Dimension;
import pythagoras.f.Rectangle;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.Button;
import tripleplay.ui.Shim;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;

public class NotebookTestScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;
    private final GameContext gameContext;

    public NotebookTestScreen(MonsterGame game) {
        super(game.plat);
        this.game = game;
        Player p1 = new Player.Builder().name("Bonnie").color(Colors.CYAN).skills(ImmutableList.of(Skill.named("Foo"))).build();
        Player p2 = new Player.Builder().name("Clyde").color(Colors.ORANGE).skills(ImmutableList.of(Skill.named("Foo"))).build();
        gameContext = new GameContext(game, p1, p2);
    }

    @Override
    public void wasAdded() {
        float w = 400, h = 300;
        final Notebook notebook = new Notebook(game, iface.anim,
                new Rectangle(game.plat.graphics().viewSize.width() / 2 - w,
                        game.plat.graphics().viewSize.height() / 2 - h / 2,
                        w * 2, h),
                new PageLayerFactory(iface, gameContext, gameContext.players.get(0), new Dimension(400, 300)).createAll());
        layer.add(notebook);

        iface.createRoot(AxisLayout.vertical(), SimpleStyles.newSheet(game.plat.graphics()), layer)
                .setSize(size())
                .add(new Button("Turn page").onClick(new Slot<Button>() {
                            @Override
                            public void onEmit(final Button button) {
                                button.setEnabled(false);
                                Encounter e = Encounter.with("Chupacabra").image("chupacabra").build();
                                gameContext.encounter.update(e);
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

    @Override
    public Game game() {
        return game;
    }
}
