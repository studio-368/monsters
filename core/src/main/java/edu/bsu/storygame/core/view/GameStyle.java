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
import edu.bsu.storygame.core.assets.AudioCache;
import edu.bsu.storygame.core.assets.Typeface;
import playn.core.Color;
import playn.core.Font;
import playn.core.Graphics;
import tripleplay.ui.*;
import tripleplay.ui.util.Insets;
import tripleplay.util.Colors;

/**
 * A stylesheet generator.
 * <p/>
 * This is based on TriplePlay's SimpleStyles class.
 *
 * @see <a href="https://github.com/threerings/tripleplay/blob/master/core/src/main/java/tripleplay/ui/SimpleStyles.java">SimpleStyles</a>
 */
public final class GameStyle {

    private static final float LARGE = 0.05f;
    private static final float REGULAR = 0.045f;
    private static final float SMALL = 0.033f;
    private static final float TINY = 0.01f;

    private static final int DARK_TRANSLUCENT_GREY = Color.argb(240, 30, 30, 30);

    private GameStyle() {
    }

    public static Stylesheet newSheet(MonsterGame game) {
        return newSheetBuilder(game).create();
    }

    public static Stylesheet.Builder newSheetBuilder(MonsterGame game) {
        final Graphics gfx = game.plat.graphics();

        int bgColor = 0xFFCCCCCC, ulColor = 0xFFEEEEEE, brColor = 0xFFAAAAAA;
        Background butBg = Background.roundRect(gfx, bgColor, 5, ulColor, 2).inset(5, 6, 2, 6);
        Background butSelBg = Background.roundRect(gfx, bgColor, 5, brColor, 2).inset(6, 5, 1, 7);

        final float cornerRadius = game.bounds.percentOfHeight(0.02f);
        final float borderWidth = game.bounds.percentOfHeight(0.005f);
        final Insets buttonInsets = new Insets(game.bounds.percentOfHeight(0.02f),
                game.bounds.percentOfHeight(0.02f),
                game.bounds.percentOfHeight(0.02f),
                game.bounds.percentOfHeight(0.02f));
        final Background palettizedButtonBg = Background.roundRect(gfx, Palette.OBSERVATORY, cornerRadius, Palette.ROSE, borderWidth)
                .insets(buttonInsets);
        final Background palettizedSelectedButtonBg = Background.roundRect(gfx, Palette.ROSE, cornerRadius, Palette.OBSERVATORY, borderWidth)
                .insets(buttonInsets);

        final Font gameTextSmall = Typeface.GAME_TEXT.in(game).atSize(SMALL);
        final Font gameTextRegular = Typeface.GAME_TEXT.in(game).atSize(REGULAR);
        final Font gameTextLarge = Typeface.GAME_TEXT.in(game).atSize(LARGE);


        return Stylesheet.builder()
                .add(Label.class,
                        Style.FONT.is(gameTextRegular),
                        Style.COLOR.is(Colors.WHITE))
                .add(Button.class,
                        Style.FONT.is(gameTextLarge),
                        Style.COLOR.is(Colors.BLACK),
                        Style.BACKGROUND.is(Background.roundRect(gfx, Palette.ROSE, cornerRadius, Palette.OBSERVATORY, borderWidth).insets(buttonInsets)),
                        Style.ACTION_SOUND.is(game.audioCache.getSound(AudioCache.Key.CLICK)))
                .add(Button.class, Style.Mode.SELECTED,
                        Style.FONT.is(gameTextLarge),
                        Style.COLOR.is(Colors.WHITE),
                        Style.BACKGROUND.is(Background.roundRect(gfx, Palette.OBSERVATORY, cornerRadius, Palette.ROSE, borderWidth).insets(buttonInsets)))
                .add(Button.class, Style.Mode.DISABLED,
                        Style.FONT.is(gameTextLarge),
                        Style.COLOR.is(Colors.LIGHT_GRAY),
                        Style.BACKGROUND.is(Background.roundRect(gfx, Palette.NOBEL, cornerRadius, Palette.SWIRL, borderWidth).insets(buttonInsets)))

                .add(ToggleButton.class,
                        Style.BACKGROUND.is(butBg))
                .add(ToggleButton.class, Style.Mode.SELECTED,
                        Style.BACKGROUND.is(butSelBg))
                .add(CheckBox.class,
                        Style.BACKGROUND.is(Background.roundRect(gfx, bgColor, 5, ulColor, 2).
                                inset(3, 2, 0, 3)))
                .add(CheckBox.class, Style.Mode.SELECTED,
                        Style.BACKGROUND.is(Background.roundRect(gfx, bgColor, 5, brColor, 2).
                                inset(3, 2, 0, 3)))
                // flip ul and br to make Field appear recessed
                .add(Field.class,
                        Style.FONT.is(gameTextRegular),
                        Style.BACKGROUND.is(Background.beveled(0xFFFFFFFF, brColor, ulColor).inset(5)),
                        Style.HALIGN.left)
                .add(Field.class, Style.Mode.DISABLED,
                        Style.BACKGROUND.is(Background.beveled(0xFFCCCCCC, brColor, ulColor).inset(5)))
                .add(Menu.class,
                        Style.BACKGROUND.is(Background.bordered(0xFFFFFFFF, 0x00000000, 1).inset(6)))
                .add(MenuItem.class,
                        Style.BACKGROUND.is(Background.solid(0xFFFFFFFF)),
                        Style.HALIGN.left)
                .add(MenuItem.class, Style.Mode.SELECTED,
                        Style.BACKGROUND.is(Background.solid(0xFF000000)),
                        Style.COLOR.is(0xFFFFFFFF))
                .add(Tabs.class,
                        Tabs.HIGHLIGHTER.is(Tabs.textColorHighlighter(0xFF000000, 0xFFFFFFFF)))
                .add(PlayerCreationGroup.SkillButton.class,
                        Style.FONT.is(gameTextSmall),
                        Style.COLOR.is(Colors.BLACK),
                        Style.BACKGROUND.is(Background.roundRect(gfx, Palette.ROSE, cornerRadius, Palette.OBSERVATORY, borderWidth).insets(buttonInsets)))
                .add(PlayerCreationGroup.SkillButton.class, Style.Mode.SELECTED,
                        Style.FONT.is(gameTextSmall),
                        Style.COLOR.is(Colors.WHITE),
                        Style.BACKGROUND.is(Background.roundRect(gfx, Palette.OBSERVATORY, cornerRadius, Palette.ROSE, borderWidth).insets(buttonInsets)))
                .add(NavigationButton.class,
                        Style.FONT.is(gameTextLarge),
                        Style.COLOR.is(Colors.BLACK),
                        Style.BACKGROUND.is(Background.roundRect(gfx, Palette.ROSE, cornerRadius, Palette.OBSERVATORY, borderWidth).insets(buttonInsets)))
                .add(NavigationButton.class, Style.Mode.SELECTED,
                        Style.FONT.is(gameTextLarge),
                        Style.COLOR.is(Colors.WHITE),
                        Style.BACKGROUND.is(Background.roundRect(gfx, Palette.OBSERVATORY, cornerRadius, Palette.ROSE, borderWidth).insets(buttonInsets)))
                .add(NavigationButton.class, Style.Mode.DISABLED,
                        Style.FONT.is(gameTextLarge),
                        Style.COLOR.is(Colors.LIGHT_GRAY),
                        Style.BACKGROUND.is(Background.roundRect(gfx, Colors.GRAY, cornerRadius, Colors.LIGHT_GRAY, borderWidth).insets(buttonInsets)))

                .add(Sidebar.NameLabel.class,
                        Style.FONT.is(gameTextLarge),
                        Style.COLOR.is(Colors.WHITE),
                        Style.HALIGN.left)
                .add(Sidebar.SkillLabel.class,
                        Style.FONT.is(gameTextRegular),
                        Style.HALIGN.left)
                .add(Sidebar.PlayerView.PointLabel.class,
                        Style.FONT.is(gameTextLarge),
                        Style.COLOR.is(Palette.ROSE),
                        Style.BACKGROUND.is(Background.bordered(Palette.BROWN_POD, Palette.ROSE, borderWidth).insets(buttonInsets)))


                .add(HandoffDialogFactory.HandoffDialog.class,
                        Style.BACKGROUND.is(Background.roundRect(gfx, DARK_TRANSLUCENT_GREY, cornerRadius)))
                .add(HandoffDialogFactory.OkButton.class,
                        Style.FONT.is(gameTextLarge),
                        Style.COLOR.is(Colors.BLACK),
                        Style.BACKGROUND.is(Background.roundRect(gfx, Palette.ROSE, cornerRadius, Palette.OBSERVATORY, borderWidth)))
                .add(HandoffDialogFactory.OkButton.class, Style.Mode.SELECTED,
                        Style.FONT.is(gameTextLarge),
                        Style.COLOR.is(Colors.WHITE),
                        Style.BACKGROUND.is(Background.roundRect(gfx, Palette.OBSERVATORY, cornerRadius, Palette.ROSE, borderWidth)));
    }
}
