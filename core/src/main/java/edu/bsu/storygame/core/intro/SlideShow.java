package edu.bsu.storygame.core.intro;

import com.google.common.collect.ImmutableList;
import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.view.GameStyle;
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

import static com.google.common.base.Preconditions.checkNotNull;

public final class SlideShow {

    private final ImmutableList<SlideData> slideDataList;
    private final MonsterGame game;
    private final Stylesheet stylesheet;

    public SlideShow(MonsterGame game, SlideData... slideData) {
        this.game = checkNotNull(game);
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

        private final class Slide extends ScreenStack.UIScreen {
            private final GroupLayer content;
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
                super(game.plat);
                this.data = checkNotNull(data);
                this.content = new GroupLayer(game.bounds.width(), game.bounds.height());
                this.layer.addCenterAt(content, game.plat.graphics().viewSize.width() / 2,
                        game.plat.graphics().viewSize.height() / 2);
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

                Group buttonGroup = new Group(AxisLayout.horizontal()).add(skip, nextButton);

                if (data.imageKey != null) {
                    root.add(new Label(Icons.image(game.imageCache.image(data.imageKey)))
                            .setConstraint(AxisLayout.stretched(0.75f)));
                    Group textGroup = new Group(new BorderLayout())
                            .addStyles(Style.BACKGROUND.is(Background.solid(Colors.BLACK)))
                            .setConstraint(AxisLayout.stretched(0.25f));
                    textGroup.add(new Label(data.text)
                            .addStyles(Style.COLOR.is(Colors.WHITE))
                            .setConstraint(BorderLayout.CENTER));
                    textGroup.add(buttonGroup.setConstraint(BorderLayout.SOUTH));
                    root.add(textGroup);
                } else {
                    root.add(new Label(data.text));
                    root.add(buttonGroup);
                }
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
                        .addStyles(Style.BACKGROUND.is(Background.solid(Colors.CYAN)))
                        .add(new Label(data.popupText).addStyles(Style.TEXT_WRAP.on));
                return popup;
            }

            @Override
            public Game game() {
                return game;
            }
        }
    }


}
