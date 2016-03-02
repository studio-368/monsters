package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.assets.Typeface;
import playn.core.Game;
import playn.core.Image;
import playn.scene.GroupLayer;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

public class StartScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;
    private Root root;
    private final GroupLayer boundedLayer;


    public StartScreen(final MonsterGame game) {
        super(game.plat);
        this.game = game;
        this.boundedLayer = new GroupLayer(game.bounds.width(), game.bounds.height());
        layer.addAt(boundedLayer,
                (game.plat.graphics().viewSize.width() - game.bounds.width()) / 2,
                (game.plat.graphics().viewSize.height() - game.bounds.height()) / 2);
        initRoot();
    }

    private void initRoot() {
        final float iconPercentOfScreen = 0.25f;
        final Image image = game.imageCache.image(ImageCache.Key.LOGO);
        final float scale = iconPercentOfScreen * size().width() / image.width();
        final Icon scaledIcon = Icons.scaled(Icons.image(image), scale);
        root = iface.createRoot(AxisLayout.vertical(), GameStyle.newSheet(game), boundedLayer)
                .setSize(game.bounds.width(), game.bounds.height())
                .addStyles(Style.BACKGROUND.is(Background.solid(Palette.TUSCANY)))
                .add(new Label("The Nightmare Defenders need your help!")
                                .addStyles(Style.FONT.is(Typeface.PASSION_ONE.in(game).atSize(0.08f)),
                                        Style.TEXT_WRAP.on,
                                        Style.COLOR.is(Palette.BLUE_LAGOON)),
                        new Label(scaledIcon),
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
                    game.screenStack.push(new PlayerCreationScreen(game), game.screenStack.slide());
                }
            });
        }

        @Override
        protected Class<?> getStyleClass() {
            return StartButton.class;
        }
    }
}