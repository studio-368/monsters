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

    @Override
    public Game game() {
        return game;
    }

    final class StartButton extends Button {
        private StartButton() {
            super("Begin Our Adventure!");
            onClick(new Slot<Button>() {
                @Override
                public void onEmit(Button button) {
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
                                    .imageKey(ImageCache.Key.MISSING_IMAGE),
                            SlideData.text("Write everything down.")
                                    .imageKey(ImageCache.Key.MISSING_IMAGE),
                            SlideData.text("Whoever finds the most inspiring stories wins!")
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