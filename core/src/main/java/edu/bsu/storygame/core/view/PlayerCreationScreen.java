package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import playn.core.Game;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;

public class PlayerCreationScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;

    public PlayerCreationScreen(MonsterGame game) {
        super(game.plat);
        this.game = game;
        Root root = iface.createRoot(AxisLayout.vertical(), SimpleStyles.newSheet(game.plat.graphics()), layer);
        root.setSize(game.bounds.width(), game.bounds.height());
        root.addStyles(Style.BACKGROUND.is(Background.solid(Colors.WHITE)));
        root.add(new Label("hi!"));
    }

    @Override
    public Game game() {
        return game;
    }
}
