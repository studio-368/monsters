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
import edu.bsu.storygame.core.assets.Typeface;
import playn.core.Game;
import react.SignalView;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.BorderLayout;
import tripleplay.ui.layout.FlowLayout;
import tripleplay.util.Colors;


public class PlayAgainScreen extends BoundedUIScreen {

    private final MonsterGame game;
    private final Stylesheet stylesheet;
    private Root root;

    public PlayAgainScreen(MonsterGame game) {
        super(game);
        this.game = game;
        this.stylesheet = GameStyle.newSheet(game);
        initRoot();
    }

    private void initRoot() {
        root = iface.createRoot(AxisLayout.vertical(), stylesheet, content)
                .setSize(content.width(), content.height())
                .add(createLayout());
    }

    private Group createLayout() {
        Group group = new Group(new BorderLayout())
                .addStyles(Style.BACKGROUND.is(Background.solid(Colors.BLACK)))
                .setConstraint(AxisLayout.stretched(1f));
        group.add(createContent().setConstraint(BorderLayout.CENTER));
        group.add(createFooter().setConstraint(BorderLayout.SOUTH));
        return group;
    }

    private Group createContent() {
        return new Group(new FlowLayout()).add(
                new Label("You both did some pretty awesome research!  Why not").addStyles(
                        Style.TEXT_WRAP.on,
                        Style.FONT.is(Typeface.GAME_TEXT.in(game).atSize(0.1f))
                ),
                new Button("Play Again").onClick(new SignalView.Listener<Button>() {
                    @Override
                    public void onEmit(Button event) {
                        game.screenStack.push(new StartScreen(game), game.screenStack.slide());
                    }
                }).addStyles(Style.FONT.is(Typeface.GAME_TEXT.in(game).atSize(0.1f))),
                new Label("and see what else you can find!").addStyles(
                        Style.TEXT_WRAP.on,
                        Style.FONT.is(Typeface.GAME_TEXT.in(game).atSize(0.1f))
                )
        );
    }

    private Group createFooter() {
        return new Group(AxisLayout.horizontal()).add(
                new Shim(0, 0).setConstraint(AxisLayout.stretched()),
                new Label("Thanks for playing!").addStyles(
                        Style.COLOR.is(Colors.WHITE)
                )
        );
    }

    @Override
    public Game game() {
        return game;
    }
}
