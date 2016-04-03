package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import playn.scene.GroupLayer;
import tripleplay.game.ScreenStack;

public abstract class BoundedUIScreen extends ScreenStack.UIScreen {

    protected final GroupLayer content;

    protected BoundedUIScreen(MonsterGame game) {
        super(game.plat);
        this.content = new GroupLayer(game.bounds.width(), game.bounds.height());
        this.layer.addCenterAt(content, game.plat.graphics().viewSize.width() / 2, game.plat.graphics().viewSize.height() / 2);
    }
}
