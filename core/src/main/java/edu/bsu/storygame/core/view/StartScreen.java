package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.intro.SlideData;
import edu.bsu.storygame.core.intro.SlideShow;
import edu.bsu.storygame.core.util.IconScaler;
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
        IconScaler scaler = new IconScaler(game);
        Icon logo = scaler.scale(ImageCache.Key.LOGO, game.bounds.width() * 0.25f);
        iface.createRoot(AxisLayout.vertical(), GameStyle.newSheet(game), content)
                .setSize(content.width(), content.height())
                .addStyles(Style.BACKGROUND.is(Background.solid(Palette.TUSCANY)))
                .add(new Label("The Nightmare Defenders need your help!")
                                .addStyles(Style.FONT.is(Typeface.PASSION_ONE.in(game).atSize(0.08f)),
                                        Style.TEXT_WRAP.on,
                                        Style.COLOR.is(Palette.BLUE_LAGOON)),
                        new Label(logo),
                        new Label("Monsters are appearing in people's nightmares all over the world")
                                .addStyles(Style.FONT.is(Typeface.OXYGEN.in(game).atSize(0.08f)),
                                        Style.TEXT_WRAP.on,
                                        Style.COLOR.is(Palette.BLUE_LAGOON)),
                        new StartButton());
    }

    @Override
    public Game game() {
        return game;
    }

    final class StartButton extends Button {
        private StartButton() {
            super("Join the Fight");
            onClick(new Slot<Button>() {
                @Override
                public void onEmit(Button button) {
                    SlideShow slideShow = new SlideShow(game,
                            SlideData.text("Hello"),
                            SlideData.text("I've heard you are good writers")
                                    .imageKey(ImageCache.Key.MISSING_IMAGE)
                                    .popupText("What's that?"),
                            SlideData.text("You need ideas for your next book?")
                                    .imageKey(ImageCache.Key.MISSING_IMAGE),
                            SlideData.text("How about monsters?")
                                    .imageKey(ImageCache.Key.MISSING_IMAGE),
                            SlideData.text("There's a great big world full of monster stories out there.")
                                    .imageKey(ImageCache.Key.MISSING_IMAGE)
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