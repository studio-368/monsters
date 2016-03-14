package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import edu.bsu.storygame.core.util.IconScaler;
import playn.core.Game;
import playn.scene.GroupLayer;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

public class WinScreen extends ScreenStack.UIScreen {

    private final GameContext context;
    private final Player winner;
    private final GroupLayer boundedLayer;
    private Root root;

    public WinScreen(final GameContext context, final Player winner) {
        super(context.game.plat);
        this.context = context;
        this.winner = winner;
        this.boundedLayer = new GroupLayer(context.game.bounds.width(), context.game.bounds.height());
        layer.addAt(boundedLayer,
                (context.game.plat.graphics().viewSize.width() - context.game.bounds.width()) / 2,
                (context.game.plat.graphics().viewSize.height() - context.game.bounds.height()) / 2);
        initRoot();
    }

    private void initRoot() {
        final MonsterGame game = context.game;
        IconScaler iconScaler = new IconScaler(game);
        Icon logo = iconScaler.scale(ImageCache.Key.LOGO, game.bounds.width() * 0.25f);
        root = iface.createRoot(AxisLayout.vertical(), GameStyle.newSheet(game), boundedLayer)
                .setSize(game.bounds.width(), game.bounds.height())
                .addStyles(Style.BACKGROUND.is(Background.solid(Palette.TUSCANY)))
                .add(new Label("CONGRATULATIONS, " + winner.name.toUpperCase() + "!")
                                .addStyles(Style.FONT.is(Typeface.PASSION_ONE.in(context.game).atSize(0.08f)),
                                        Style.TEXT_WRAP.on,
                                        Style.COLOR.is(Palette.BLUE_LAGOON)),
                        new Label(logo),
                        new Label("You have saved the world from Monsters.")
                                .addStyles(Style.FONT.is(Typeface.OXYGEN.in(game).atSize(0.08f)),
                                        Style.TEXT_WRAP.on,
                                        Style.COLOR.is(Palette.BLUE_LAGOON)),
                        new Label("You are a hero.")
                                .addStyles(Style.FONT.is(Typeface.PASSION_ONE.in(game).atSize(0.08f)),
                                        Style.TEXT_WRAP.on,
                                        Style.COLOR.is(Palette.BLUE_LAGOON)),
                        new RestartButton());

    }

    @Override
    public Game game() {
        return context.game;
    }

    private final class RestartButton extends Button {
        private RestartButton() {
            super("Play again");
            onClick(new Slot<Button>() {
                @Override
                public void onEmit(Button button) {
                    context.game.screenStack.push(new PlayerCreationScreen(context.game), context.game.screenStack.slide());
                }
            });
        }

        @Override
        protected Class<?> getStyleClass() {
            return PlayerCreationScreen.StartButton.class;
        }
    }

}
