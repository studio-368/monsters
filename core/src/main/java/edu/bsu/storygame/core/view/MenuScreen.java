package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import playn.core.Game;
import playn.scene.Pointer;
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
        initPointer();
        initRoot();
        initUIVariables();
    }

    private void initPointer(){
        new Pointer(game().plat, layer, true);
    }


    private void initRoot(){
        root = iface.createRoot(AxisLayout.vertical().gap(50), SimpleStyles.newSheet(game.plat.graphics()), layer);
        root.setSize(game.bounds.width(), game.bounds.height());
        root.addStyles(Style.BACKGROUND.is(Background.solid(Colors.WHITE)));
    }

    private void initUIVariables(){
        root.add(new Label("Nightmare Defenders").addStyles(game.bigLabel.add(Style.TEXT_EFFECT.shadow)));
        root.add(new Button("Begin Game").addStyles(game.bigButton).onClick(new Slot<Button>() {
            @Override
            public void onEmit(Button button) {
                game.screenStack.push(new SampleGameScreen(game), game.screenStack.slide());
            }
        }));
    }

    @Override
    public Game game() {
        return game;
    }
}