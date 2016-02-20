package edu.bsu.storygame.core;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.assets.TileCache;
import edu.bsu.storygame.core.util.AspectRatioTool;
import edu.bsu.storygame.core.util.GameBounds;
import playn.core.Platform;
import playn.scene.Mouse;
import playn.scene.Pointer;
import playn.scene.SceneGame;
import pythagoras.f.IRectangle;
import tripleplay.game.ScreenStack;

public class MonsterGame extends SceneGame {

    private static final float ASPECT_RATIO = 16f / 10f;

    private static final int UPDATE_RATE_MS = 33; // 30 times per second

    public final ImageCache imageCache;
    public final TileCache tileCache;
    public final GameBounds bounds;
    public final ScreenStack screenStack;

    public MonsterGame(Platform plat) {
        super(plat, UPDATE_RATE_MS);
        imageCache = new ImageCache(plat.assets());
        tileCache = new TileCache(plat.assets());
        initInput();
        this.bounds = initAspectRatio();
        screenStack = new ScreenStack(this, rootLayer);
        screenStack.push(new LoadingScreen(this, screenStack));
    }

    private void initInput() {
        new Pointer(plat, rootLayer, true);
        plat.input().mouseEvents.connect(new Mouse.Dispatcher(rootLayer, true));
    }

    private GameBounds initAspectRatio() {
        IRectangle aspectRatioBox = new AspectRatioTool(ASPECT_RATIO)
                .createBoundingBoxWithin(plat.graphics().viewSize);
        return new GameBounds(aspectRatioBox);
    }
}
