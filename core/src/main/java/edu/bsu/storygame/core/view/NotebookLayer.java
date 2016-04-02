package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.model.GameContext;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import pythagoras.f.IDimension;
import tripleplay.anim.Animator;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.BorderLayout;
import tripleplay.util.Layers;

public final class NotebookLayer extends GroupLayer {

    public static final float OPEN_CLOSE_ANIM_DURATION = 400f;

    private final GroupLayer left;
    private final Layer right;

    public NotebookLayer(int color, IDimension closedSize, GameContext context) {
        super(closedSize.width() * 2, closedSize.height());
        setOrigin(Origin.TC);

        this.left = new GroupLayer(closedSize.width(), closedSize.height());
        this.right = Layers.solid(color, closedSize.width(), closedSize.height());

        addAt(left, closedSize.width(), 0);
        addAt(right, closedSize.width(), 0);

        this.left.setDepth(1);
        this.right.setDepth(0);

        Stylesheet stylesheet = GameStyle.newSheet(context.game);
        Interface iface = ((ScreenStack.UIScreen) context.game.screenStack.top()).iface;

        Root root = iface.createRoot(new BorderLayout(), stylesheet, this.left)
                .setSize(closedSize)
                .addStyles(Style.BACKGROUND.is(Background.solid(color)));
        root.add(new Label("Notebook!")
                .setConstraint(BorderLayout.NORTH));
    }

    public void open(Animator anim) {
        anim.tweenX(left)
                .to(0)
                .in(OPEN_CLOSE_ANIM_DURATION)
                .easeIn();
    }

    public void close(Animator anim) {
        anim.tweenX(left)
                .to(width() / 2)
                .in(OPEN_CLOSE_ANIM_DURATION)
                .easeIn();
    }

}