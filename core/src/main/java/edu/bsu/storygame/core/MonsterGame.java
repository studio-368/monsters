/*
 * Copyright 2016 Traveler's Notebook: Monster Tales project authors
 *
 * This file is part of monsters
 *
 * monsters is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * monsters is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with monsters.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.bsu.storygame.core;

import edu.bsu.storygame.core.assets.AudioCache;
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
import react.UnitSignal;
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
    public final AudioCache audioCache;
    public final GameBounds bounds;
    public final ScreenStack screenStack;
    public final NarrativeCache narrativeCache;
    public final Pointer pointer;

    public final UnitSignal onGameStart = new UnitSignal();
    public final UnitSignal onGameEnd = new UnitSignal();

    public MonsterGame(Config config) {
        super(config.platform, UPDATE_RATE_MS);
        audioCache = new AudioCache(plat.assets());
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
