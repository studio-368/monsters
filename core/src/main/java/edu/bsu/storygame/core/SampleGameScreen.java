package edu.bsu.storygame.core;

import playn.core.Game;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class SampleGameScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;

    public SampleGameScreen(MonsterGame game) {
        super(checkNotNull(game).plat);
        this.game = game;
        createUI();
    }

    private void createUI() {
        iface.createRoot(AxisLayout.vertical(), SimpleStyles.newSheet(game.plat.graphics()), layer)
                .setSize(size())
                .setStyles(Style.BACKGROUND.is(Background.image(game.plat.assets().getImage("images/bg.png"))))
                .add(new Label("Welcome to the PlayN Game Prototype"));
    }

    @Override
    public Game game() {
        return game;
    }
}
