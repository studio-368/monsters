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

package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.Typeface;
import playn.core.Game;
import react.Slot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.FlowLayout;

public class PlayerNameScreen extends BoundedUIScreen {

    private static final float NAME_FIELD_WIDTH_PERCENT = 0.25f;
    private static final float NAME_FIELD_HEIGHT_PERCENT = 0.08f;
    private final MonsterGame game;
    private final Root root;
    private Field nameFieldOne;
    private Field nameFieldTwo;
    private Button continueButton = new Button("Done");

    public PlayerNameScreen(final MonsterGame game) {
        super(game);
        this.game = game;
        root = iface.createRoot(AxisLayout.vertical().offStretch(),
                GameStyle.newSheet(game), content)
                .setSize(content.width(), content.height());
        root.addStyles(Style.BACKGROUND.is(Background.solid(Palette.GOLDEN_POPPY)));
        root.add(new Label("Traveler's Notebook: Monster Tales").addStyles(Style.FONT.is(Typeface.GAME_TEXT.in(game).atSize(0.10f))));
        root.add(new Label("Please enter your names:").addStyles(Style.FONT.is(Typeface.GAME_TEXT.in(game).atSize(0.05f))));
        continueButton.onClick(new Slot<Button>() {
            @Override
            public void onEmit(Button button) {
                game.screenStack.push(new PlayerCreationScreen(game, new String[]{
                        nameFieldOne.text.get().trim(), nameFieldTwo.text.get().trim()
                }), game.screenStack.slide());
            }
        });
        root.add(new Group(AxisLayout.horizontal().offStretch().stretchByDefault().gap(0))
                .add(createNameArea(1), createNameArea(2))
                .setConstraint(Constraints.fixedHeight(game.bounds.percentOfHeight(0.65f))));
        root.add(new Group(new FlowLayout())
                .add(continueButton.setEnabled(false)));
    }

    private Group createNameArea(int player) {
        Group group = new Group(AxisLayout.vertical());
        group.add(new Label("Player " + player + ":"));
        int color = Palette.PLAYER_TWO;
        if (player == 1) {
            color = Palette.PLAYER_ONE;
            group.add(nameFieldOne = new Field()
                    .setConstraint(Constraints.fixedSize(game.bounds.width() * NAME_FIELD_WIDTH_PERCENT,
                            game.bounds.height() * NAME_FIELD_HEIGHT_PERCENT)));
            nameFieldOne.text.connect(new Slot<String>() {
                @Override
                public void onEmit(String s) {
                    checkForCompletion();
                }
            });
            nameFieldOne.setVisible(false);
        } else {
            group.add(nameFieldTwo = new Field()
                    .setConstraint(Constraints.fixedSize(game.bounds.width() * NAME_FIELD_WIDTH_PERCENT,
                            game.bounds.height() * NAME_FIELD_HEIGHT_PERCENT)));
            nameFieldTwo.text.connect(new Slot<String>() {
                @Override
                public void onEmit(String s) {
                    checkForCompletion();
                }
            });
            nameFieldTwo.setVisible(false);
        }
        group.addStyles(Style.BACKGROUND.is(Background.solid(color)));
        return group;
    }

    private void checkForCompletion() {
        final boolean isComplete = !nameFieldOne.text.get().trim().isEmpty() && !nameFieldTwo.text.get().trim().isEmpty();
        continueButton.setEnabled(isComplete);
    }

    @Override
    public Game game() {
        return game;
    }

    @Override
    public void showTransitionCompleted() {
        nameFieldOne.setVisible(true);
        nameFieldTwo.setVisible(true);
    }

    @Override
    public void hideTransitionStarted() {
        Container.removeFromParent(nameFieldOne, true);
        Container.removeFromParent(nameFieldTwo, true);
    }

}
