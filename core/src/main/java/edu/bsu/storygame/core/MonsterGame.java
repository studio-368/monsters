package edu.bsu.storygame.core;

import playn.core.Image;
import playn.core.Platform;
import playn.scene.ImageLayer;
import playn.scene.SceneGame;
import tripleplay.game.ScreenStack;

public class MonsterGame extends SceneGame {

    private static final int UPDATE_RATE_MS = 33; // 30 times per second

    public MonsterGame(Platform plat) {
        super(plat, UPDATE_RATE_MS);
        ScreenStack screenStack = new ScreenStack(this, rootLayer);
        screenStack.push(new SampleGameScreen(this));
    }
}
