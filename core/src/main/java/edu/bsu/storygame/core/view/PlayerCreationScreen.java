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
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import playn.core.Game;
import react.Slot;
import react.Values;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.FlowLayout;

public final class PlayerCreationScreen extends BoundedUIScreen {

    private final MonsterGame game;
    private final Root root;
    private final PlayerCreationGroup playerOneGroup;
    private final PlayerCreationGroup playerTwoGroup;
    private final Button startButton;
    private String[] players;

    public PlayerCreationScreen(final MonsterGame game, String[] players) {
        super(game);
        this.game = game;
        this.players = players;
        root = iface.createRoot(AxisLayout.vertical().offStretch(),
                GameStyle.newSheet(game), content)
                .setSize(content.width(), content.height());
        root.addStyles(Style.BACKGROUND.is(Background.solid(Palette.GOLDEN_POPPY)));
        root.add(new Label("Traveler's Notebook: Monster Tales").addStyles(Style.FONT.is(Typeface.GAME_TEXT.in(game).atSize(0.10f))));
        root.add(new Label("Please customize your characters:").addStyles(Style.FONT.is(Typeface.GAME_TEXT.in(game).atSize(0.05f))));
        playerOneGroup = createPlayerGroup(Palette.PLAYER_ONE, players[0]);
        playerTwoGroup = createPlayerGroup(Palette.PLAYER_TWO, players[1]);
        root.add(new Group(AxisLayout.horizontal().offStretch().stretchByDefault().gap(0))
                .add(playerOneGroup, playerTwoGroup)
                .setConstraint(Constraints.fixedHeight(game.bounds.percentOfHeight(0.65f)))
        );
        startButton = new Button("Start");
        startButton.onClick(new Slot<Button>() {
            @Override
            public void onEmit(Button button) {
                game.screenStack.push(new GameScreen(createGameContext()), game.screenStack.slide());
            }
        });
        root.add(new Group(new FlowLayout())
                .add(startButton.setEnabled(false)));

        Values.and(playerOneGroup.complete, playerTwoGroup.complete).connect(startButton.enabledSlot());
    }

    private PlayerCreationGroup createPlayerGroup(final int color, String playerName) {
        PlayerCreationGroup group = new PlayerCreationGroup(game, playerName);
        group.addStyles(Style.BACKGROUND.is(Background.solid(color)),
                Style.VALIGN.top);
        return group;
    }

    private GameContext createGameContext() {
        Player p1 = new Player.Builder()
                .name(players[0])
                .color(Palette.PLAYER_ONE)
                .skills(playerOneGroup.getSelectedSkills())
                .location(playerOneGroup.getSelectedRegion())
                .build();
        Player p2 = new Player.Builder()
                .name(players[1])
                .color(Palette.PLAYER_TWO)
                .skills(playerTwoGroup.getSelectedSkills())
                .location(playerTwoGroup.getSelectedRegion())
                .build();
        return new GameContext(game, p1, p2);
    }

    @Override
    public Game game() {
        return game;
    }
}
