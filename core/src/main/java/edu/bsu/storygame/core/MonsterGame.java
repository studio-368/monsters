package edu.bsu.storygame.core;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.assets.NarrativeCache;
import edu.bsu.storygame.core.model.Narrative;
import edu.bsu.storygame.core.util.AspectRatioTool;
import edu.bsu.storygame.core.util.GameBounds;
import playn.core.Platform;
import playn.scene.Mouse;
import playn.scene.Pointer;
import playn.scene.SceneGame;
import pythagoras.f.IRectangle;
import tripleplay.game.ScreenStack;

import static com.google.common.base.Preconditions.checkNotNull;

public class MonsterGame extends SceneGame {

    private static final float ASPECT_RATIO = 16f / 10f;

    private static final int UPDATE_RATE_MS = 33; // 30 times per second

    public static final class Config {
        public Platform platform;
        public Narrative narrativeOverride;
        public boolean debugMode = false;

        public Config(Platform plat) {
            this.platform = checkNotNull(plat);
        }
    }

    public final ImageCache imageCache;
    public final GameBounds bounds;
    public final ScreenStack screenStack;
    public final NarrativeCache narrativeCache;
    public final Pointer pointer;

    public MonsterGame(Config config) {
        super(config.platform, UPDATE_RATE_MS);
        imageCache = new ImageCache(plat.assets());
        narrativeCache =
                config.narrativeOverride == null
                        ? new NarrativeCache.Default(this)
                        : new NarrativeCache.Overridden(config.narrativeOverride);
        this.pointer = initInput();
        if (config.debugMode) {
            plat.input().keyboardEvents.connect(new KeystrokeBasedPlayerGenerator(this));
        }
        this.bounds = initAspectRatio();
        screenStack = new ScreenStack(this, rootLayer);
        screenStack.push(new LoadingScreen(this, screenStack));
    }

    private Pointer initInput() {
        Pointer p = new Pointer(plat, rootLayer, true);
        plat.input().mouseEvents.connect(new Mouse.Dispatcher(rootLayer, true));
        return p;
    }

    private GameBounds initAspectRatio() {
        IRectangle aspectRatioBox = new AspectRatioTool(ASPECT_RATIO)
                .createBoundingBoxWithin(plat.graphics().viewSize);
        return new GameBounds(aspectRatioBox);
    }
}
