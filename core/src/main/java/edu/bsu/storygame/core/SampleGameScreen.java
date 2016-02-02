package edu.bsu.storygame.core;

import playn.core.Game;
import playn.scene.Mouse;
import playn.scene.Pointer;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class SampleGameScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;
    private final GameContext context;

    public SampleGameScreen(MonsterGame game) {
        super(checkNotNull(game).plat);
        this.game = game;
        this.context = new GameContext(game);
        configurePointerInput();
        createUI();
    }

    private void configurePointerInput() {
        new Pointer(game().plat, layer, true);
        game().plat.input().mouseEvents.connect(new Mouse.Dispatcher(layer, true));
    }

    private void createUI() {
        iface.createRoot(AxisLayout.vertical().offStretch(), SimpleStyles.newSheet(game.plat.graphics()), layer)
                .setSize(size())
                .setStyles(Style.BACKGROUND.is(Background.image(game.plat.assets().getImage("images/bg.png"))))
                .add(new Label(context.phase.get().name()) {
                         {
                             updateText();
                             SampleGameScreen.this.context.phase.connect(new Slot<Phase>() {
                                 @Override
                                 public void onEmit(Phase phase) {
                                     updateText();
                                 }
                             });
                         }
                         private void updateText() {
                             text.update("Current phase: " + context.phase.get().name());
                         }
                     },
                        new MapView(context));
    }

    @Override
    public Game game() {
        return game;
    }
}
