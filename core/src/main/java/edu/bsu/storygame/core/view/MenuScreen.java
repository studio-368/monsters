package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import playn.core.Font;
import playn.core.Game;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;

public class MenuScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;
    private Root root;

    public MenuScreen(final MonsterGame game) {
        super(game.plat);
        this.game = game;
        initRoot();
        initUIVariables();
    }

    private void initRoot() {
        root = iface.createRoot(AxisLayout.vertical().gap(50), SimpleStyles.newSheet(game.plat.graphics()), layer);
        root.setSize(game.bounds.width(), game.bounds.height());
        root.addStyles(Style.BACKGROUND.is(Background.solid(Colors.WHITE)));
    }

    private void initUIVariables() {
        root.add(new Label("Nightmare Defenders")
                .addStyles(Style.FONT.is(new Font("Times New Roman", 108)),
                        Style.HALIGN.center,
                        Style.TEXT_EFFECT.shadow));
        root.add(new Button("Begin Game")
                .addStyles(Style.FONT.is(new Font("Times New Roman", 50)),
                        Style.HALIGN.center)
                .onClick(new Slot<Button>() {
                    @Override
                    public void onEmit(Button button) {
                        game.screenStack.push(new PlayerCreationScreen(game), game.screenStack.slide());
                    }
                }));
    }

    @Override
    public Game game() {
        return game;
    }
}