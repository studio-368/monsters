package edu.bsu.storygame.core;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.assets.TileCache;
import edu.bsu.storygame.core.util.AspectRatioTool;
import edu.bsu.storygame.core.util.GameBounds;
import edu.bsu.storygame.core.view.SampleGameScreen;
import playn.core.Font;
import playn.core.Platform;
import playn.core.Pointer;
import playn.scene.SceneGame;
import pythagoras.f.IRectangle;
import tripleplay.game.ScreenStack;
import tripleplay.ui.Style;
import tripleplay.ui.Styles;

public class MonsterGame extends SceneGame {

    private static final float ASPECT_RATIO = 16f/10f;

    private static final int UPDATE_RATE_MS = 33; // 30 times per second

    public final ImageCache imageCache;
    public final TileCache tileCache;
    public final GameBounds bounds;
    public final ScreenStack screenStack;
    public Styles bigLabel;
    public Styles bigButton;

    public MonsterGame(Platform plat) {
        super(plat, UPDATE_RATE_MS);
        imageCache = new ImageCache(plat.assets());
        tileCache = new TileCache(plat.assets());
        this.bigLabel = Styles.make(
                Style.FONT.is(new Font("Times New Roman", 108)),
                Style.HALIGN.center);
        this.bigButton = Styles.make(
                Style.FONT.is(new Font("Times New Roman", 50)),
                Style.HALIGN.center);

        IRectangle aspectRatioBox = new AspectRatioTool(ASPECT_RATIO)
                .createBoundingBoxWithin(plat.graphics().viewSize);
        this.bounds = new GameBounds(aspectRatioBox);

        screenStack = new ScreenStack(this, rootLayer);
        screenStack.push(new LoadingScreen(this, screenStack));
    }
}
