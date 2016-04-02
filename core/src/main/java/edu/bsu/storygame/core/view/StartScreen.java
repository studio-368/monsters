package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.util.IconScaler;
import playn.core.Game;
import playn.scene.GroupLayer;
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
        IconScaler scaler = new IconScaler(game);
        Icon logo = scaler.scale(ImageCache.Key.LOGO, game.bounds.width() * 0.25f);
        root = iface.createRoot(AxisLayout.vertical(), GameStyle.newSheet(game), boundedLayer)
                .setSize(game.bounds.width(), game.bounds.height())
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
                        new NavigationButton("Join the Fight!", new PlayerNameScreen(game)));
    }

    @Override
    public Game game() {
        return game;
    }

}