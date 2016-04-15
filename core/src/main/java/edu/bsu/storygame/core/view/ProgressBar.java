package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.model.GameContext;
import playn.core.Surface;
import playn.scene.Layer;

import static com.google.common.base.Preconditions.checkArgument;

public class ProgressBar extends Layer {

    private int value;
    private final int max;
    private final float width;
    private final float height;
    private final GameContext context;

    public ProgressBar(int max, float width, float height, GameContext context) {
        checkArgument(max >= 0);
        this.context = context;
        this.max = max;
        this.width = width;
        this.height = height;
    }

    public void increment(int points) {
        value = points;
        if (value > max) {
            context.game.plat.log().warn("Value (" + value + ") exceeds max (" + max + "); capping.");
            value = max;
        }
    }

    @Override
    public float width() {
        return width;
    }

    @Override
    public float height() {
        return height;
    }

    @Override
    protected void paintImpl(Surface surf) {
        final float percent = value / (float) max;
        surf.setFillColor(Palette.WHITE_SMOKE);
        surf.fillRect(0, 0, width(), height());
        surf.setFillColor(Palette.GOLDEN_POPPY);
        surf.fillRect(0, height(), width(), -height() * percent);
    }
}
