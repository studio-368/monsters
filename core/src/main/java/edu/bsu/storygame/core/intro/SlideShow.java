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

package edu.bsu.storygame.core.intro;

import com.google.common.collect.ImmutableList;
import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.util.IconScaler;
import edu.bsu.storygame.core.view.BoundedUIScreen;
import edu.bsu.storygame.core.view.GameStyle;
import playn.core.Color;
import playn.core.Game;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import react.RFuture;
import react.RPromise;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.BorderLayout;
import tripleplay.util.Colors;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.*;

public final class SlideShow {
    public static final int POPUP_BACKGROUND = Color.rgb(240, 197, 0);

    private final ImmutableList<SlideData> slideDataList;
    private final MonsterGame game;
    private final Stylesheet stylesheet;
    private final IconScaler scaler;

    public SlideShow(MonsterGame game, SlideData... slideData) {
        this.game = checkNotNull(game);
        this.scaler = new IconScaler(game);
        this.slideDataList = ImmutableList.copyOf(slideData);
        this.stylesheet = GameStyle.newSheet(game);
    }

    public RFuture<Void> startOn(ScreenStack screenStack) {
        Show show = new Show(screenStack);
        screenStack.push(show.currentSlide, screenStack.slide());
        return show.promise;
    }

    public final class Show {
        private final RPromise<Void> promise = RPromise.create();

        private final ScreenStack screenStack;
        private final List<SlideData> remaining;
        private ScreenStack.UIScreen currentSlide;

        private Show(ScreenStack screenStack) {
            this.screenStack = checkNotNull(screenStack);
            this.remaining = new ArrayList<>(slideDataList);
            this.currentSlide = new Slide(remaining.remove(0));
        }

        private void advance() {
            if (remaining.isEmpty()) {
                promise.succeed(null);
            } else {
                currentSlide = new Slide(remaining.remove(0));
                screenStack.replace(currentSlide, screenStack.slide());
            }
        }

        private final class Slide extends BoundedUIScreen {
            private final SlideData data;
            private boolean popupShown = false;
            private final Button nextButton = new Button("Next").onClick(new Slot<Button>() {
                @Override
                public void onEmit(Button button) {
                    if (isTherePopupTextToShow()) {
                        showPopupText();
                    } else {
                        advance();
                    }
                }

                private boolean isTherePopupTextToShow() {
                    return data.popupText != null && !popupShown;
                }
            });

            public Slide(SlideData data) {
                super(game);
                this.data = checkNotNull(data);
            }

            @Override
            public void wasShown() {
                super.wasShown();
                Button skip = new Button("Skip").onClick(new Slot<Button>() {
                    @Override
                    public void onEmit(Button button) {
                        promise.fail(null);
                    }
                });
                Root root = iface.createRoot(AxisLayout.vertical().offStretch(), stylesheet, content)
                        .setSize(content.width(), content.height())
                        .addStyles(Style.BACKGROUND.is(Background.solid(Colors.GRAY)));

                Group buttonGroup = new Group(AxisLayout.horizontal()).add(new Shim(0, 0).setConstraint(AxisLayout.stretched()), skip, nextButton);
                if (data.imageKey != null) {
                    Icon image = scaler.scale(data.imageKey, game.bounds.width());
                    root.add(new Label(image)
                            .setConstraint(AxisLayout.stretched(0.75f)));
                    root.add(createSplitScreenGroup(buttonGroup));
                } else {
                    root.add(createFullScreenGroup(buttonGroup));
                }
            }

            private Group createSplitScreenGroup(Group buttonGroup){
                Group group = new Group(new BorderLayout())
                        .addStyles(Style.BACKGROUND.is(Background.solid(Colors.BLACK)))
                        .setConstraint(AxisLayout.stretched(0.25f));
                group.add(new Label(data.text)
                        .addStyles(Style.COLOR.is(Colors.WHITE))
                        .setConstraint(BorderLayout.CENTER));
                group.add(buttonGroup.setConstraint(BorderLayout.SOUTH));
                return group;
            }

            private Group createFullScreenGroup(Group buttonGroup){
                Group group = new Group(new BorderLayout())
                        .addStyles(Style.BACKGROUND.is(Background.solid(Colors.BLACK))).setConstraint(AxisLayout.stretched(1f));
                group.add(new Label(data.text)
                        .addStyles(Style.COLOR.is(Colors.WHITE),
                                Style.FONT.is(Typeface.GAME_TEXT.in(game).atSize(calculateSize(data.text))),
                                Style.TEXT_WRAP.on)
                        .setConstraint(BorderLayout.CENTER));
                group.add(buttonGroup.setConstraint(BorderLayout.SOUTH));
                return group;
            }

            private float calculateSize(String text) {
                if (text.length() > 100)
                    return 0.09f;
                if (text.length() > 35)
                    return 0.15f;
                if (text.length() > 15)
                    return 0.20f;
                return 0.25f;
            }

            private void showPopupText() {
                checkNotNull(data.popupText);
                nextButton.setEnabled(false);
                GroupLayer popup = makePopup();
                content.addCenterAt(popup, content.width() * 1.5f, content.height() * 2f / 3f);
                iface.anim.tweenX(popup)
                        .to(content.width() / 2)
                        .in(350f)
                        .easeIn()
                        .then()
                        .action(new Runnable() {
                            @Override
                            public void run() {
                                nextButton.setEnabled(true);
                            }
                        });
                popupShown = true;
            }

            private GroupLayer makePopup() {
                GroupLayer popup = new GroupLayer(content.width() / 2, content.height() / 2);
                popup.setOrigin(Layer.Origin.CENTER);
                iface.createRoot(AxisLayout.vertical(), stylesheet, popup)
                        .setSize(popup.width(), popup.height())
                        .addStyles(Style.BACKGROUND.is(Background.solid(POPUP_BACKGROUND)))
                        .add(new Label(data.popupText).addStyles(Style.TEXT_WRAP.on,
                                Style.COLOR.is(Colors.BLACK),
                                Style.FONT.is(Typeface.HANDWRITING.in(game).atSize(0.1f))));
                return popup;
            }

            @Override
            public Game game() {
                return game;
            }
        }
    }


}
