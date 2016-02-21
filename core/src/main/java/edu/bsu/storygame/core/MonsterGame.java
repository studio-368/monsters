package edu.bsu.storygame.core;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.assets.TileCache;
import edu.bsu.storygame.core.json.NarrativeParser;
import edu.bsu.storygame.core.model.Narrative;
import edu.bsu.storygame.core.util.AspectRatioTool;
import edu.bsu.storygame.core.util.GameBounds;
import playn.core.Platform;
import playn.scene.Mouse;
import playn.scene.Pointer;
import playn.scene.SceneGame;
import pythagoras.f.IRectangle;
import react.RFuture;
import react.RPromise;
import react.Slot;
import tripleplay.game.ScreenStack;

public class MonsterGame extends SceneGame {

    private static final float ASPECT_RATIO = 16f / 10f;

    private static final int UPDATE_RATE_MS = 33; // 30 times per second

    public final ImageCache imageCache;
    public final TileCache tileCache;
    public final GameBounds bounds;
    public final ScreenStack screenStack;
    public final RFuture<Narrative> narrative = RPromise.create();

    public MonsterGame(Platform plat) {
        super(plat, UPDATE_RATE_MS);
        imageCache = new ImageCache(plat.assets());
        tileCache = new TileCache(plat.assets());
        loadNarrative();
        initInput();
        this.bounds = initAspectRatio();
        screenStack = new ScreenStack(this, rootLayer);
        screenStack.push(new LoadingScreen(this, screenStack));
    }

    private void loadNarrative() {
        plat.assets().getText("encounters/narrative.json").onSuccess(new Slot<String>() {
            @Override
            public void onEmit(String s) {
                NarrativeParser parser = new NarrativeParser(plat.json());
                ((RPromise<Narrative>) narrative).succeed(parser.parse(s));
            }
        }).onFailure(new Slot<Throwable>() {
            @Override
            public void onEmit(Throwable throwable) {
                plat.log().error("Could not load narrative: " + throwable.getMessage());
                throw new IllegalStateException(throwable);
            }
        });
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
