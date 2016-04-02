package edu.bsu.storygame.core.view;

import playn.scene.GroupLayer;
import playn.scene.Layer;
import pythagoras.f.IDimension;
import tripleplay.anim.Animator;
import tripleplay.util.Layers;

public final class NotebookLayer extends GroupLayer {

    public static final float OPEN_CLOSE_ANIM_DURATION = 400f;

    private final Layer left;
    private final Layer right;

    public NotebookLayer(int color, IDimension closedSize) {
        super(closedSize.width() * 2, closedSize.height());
        setOrigin(Origin.TC);
        this.left = Layers.solid(color, closedSize.width(), closedSize.height());
        this.right = Layers.solid(color, closedSize.width(), closedSize.height());
        addAt(left, closedSize.width(), 0);
        addAt(right, closedSize.width(), 0);
    }

    public void open(Animator anim) {
        anim.tweenX(left)
                .to(0)
                .in(OPEN_CLOSE_ANIM_DURATION)
                .easeIn();
    }

    public void close(Animator anim) {
        anim.tweenX(left)
                .to(width())
                .in(OPEN_CLOSE_ANIM_DURATION)
                .easeIn();
    }

}