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

package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.intro.SlideData;
import edu.bsu.storygame.core.intro.SlideShow;
import playn.core.Game;
import react.SignalView;
import react.Slot;
import react.Try;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

public final class StartScreen extends BoundedUIScreen {

    private final MonsterGame game;

    public StartScreen(final MonsterGame game) {
        super(game);
        this.game = game;
        initRoot();
    }

    private void initRoot() {
        iface.createRoot(AxisLayout.vertical(), GameStyle.newSheet(game), content)
                .setSize(content.width(), content.height())
                .addStyles(Style.BACKGROUND.is(Background.image(game.imageCache.image(ImageCache.Key.MAIN_MENU_BG))))
                .add(new Shim(0, game.bounds.percentOfHeight(0.20f)),
                        new Label("Traveler's Notebook: Monster Tales")
                                .addStyles(Style.FONT.is(Typeface.TITLE_SCREEN.font.derive(
                                        game.bounds.percentOfHeight(0.08f)))),
                        new Shim(0, game.bounds.percentOfHeight(0.02f)),
                        new StartButton());
    }

    final class StartButton extends Button {
        private StartButton() {
            super("Begin Our Adventure!");
            onClick(new Slot<Button>() {
                @Override
                public void onEmit(Button button) {
                    game.onGameStart.emit();
                    SlideShow slideShow = new SlideShow(game,
                            SlideData.text("Hello"),
                            SlideData.text("I've heard you are great writers")
                                    .imageKey(ImageCache.Key.INTRO_SCENE_1)
                                    .popupText("What's that?"),
                            SlideData.text("You need ideas for your next book?")
                                    .imageKey(ImageCache.Key.INTRO_SCENE_2),
                            SlideData.text("How about monsters?")
                                    .imageKey(ImageCache.Key.INTRO_SCENE_3),
                            SlideData.text("There's a great big world full of monster stories out there.")
                                    .imageKey(ImageCache.Key.INTRO_SCENE_4)
                                    .popupText("You should explore it!"),
                            SlideData.text("Go on some adventures.")
                                    .imageKey(ImageCache.Key.INTRO_SCENE_5),
                            SlideData.text("Write everything down.")
                                    .imageKey(ImageCache.Key.INTRO_SCENE_6),
                            SlideData.text("Be the first player to reach 100 Inspiration Points!")
                    );
                    slideShow.startOn(game.screenStack).onComplete(new SignalView.Listener<Try<Void>>() {
                        @Override
                        public void onEmit(Try<Void> voidTry) {
                            game.screenStack.push(new PlayerNameScreen(game), game.screenStack.slide());
                        }
                    });
                }
            });
        }

        @Override
        protected Class<?> getStyleClass() {
            return NavigationButton.class;
        }
    }
}
