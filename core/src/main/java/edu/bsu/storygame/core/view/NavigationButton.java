package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.Button;

public class NavigationButton extends Button {


    public NavigationButton(String name, final ScreenStack.UIScreen screen) {
        super(name);
        final MonsterGame game = (MonsterGame) screen.game();
        onClick(new Slot<Button>() {
            @Override
            public void onEmit(Button button) {
                game.screenStack.push(screen, game.screenStack.slide());
            }
        });
    }

    @Override
    protected Class<?> getStyleClass() {
        return NavigationButton.class;
    }
}