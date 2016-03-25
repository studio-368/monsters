package edu.bsu.storygame.core.view;

import playn.scene.Layer;
import pythagoras.f.IDimension;
import tripleplay.util.Layers;

public final class NotebookLayer {

    public final Layer layer;

    public NotebookLayer(int color, IDimension size) {
        this.layer = Layers.solid(color, size.width(), size.height());
    }


}
