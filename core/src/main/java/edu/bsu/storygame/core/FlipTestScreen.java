/*
 * Copyright 2016 Traveler's Notebook: Monster Tales project authors
 *
 * This file is part of Traveler's Notebook: Monster Tales
 *
 * Traveler's Notebook: Monster Tales is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Traveler's Notebook: Monster Tales is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Traveler's Notebook: Monster Tales.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.bsu.storygame.core;

import playn.core.Game;
import playn.scene.Layer;
import pythagoras.f.FloatMath;
import react.Slot;
import tripleplay.anim.Animation;
import tripleplay.game.ScreenStack;
import tripleplay.shaders.RotateYBatch;
import tripleplay.ui.Button;
import tripleplay.ui.Shim;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;
import tripleplay.util.Interpolator;
import tripleplay.util.Layers;

public class FlipTestScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;
    private final Layer page;
    private final Interpolator interp = Interpolator.EASE_INOUT;
    private final float duration = 4000f;
    private RotateYBatch batch;

    public FlipTestScreen(MonsterGame game) {
        super(game.plat);
        this.game = game;

        page = Layers.solid(Colors.YELLOW, 400, 300);
        layer.addAt(page, game.plat.graphics().viewSize.width()/2, 200);
    }

    @Override
    public void wasAdded() {
        iface.createRoot(AxisLayout.vertical(), SimpleStyles.newSheet(game.plat.graphics()), layer)
                .setSize(game.plat.graphics().viewSize)
                .add(new Button("Do it").onClick(new Slot<Button>() {
                    public void onEmit(Button button) {
                        startAnimation();
                    }
                }))
                .add(new Shim(0, 0).setConstraint(AxisLayout.stretched()));
    }

    private void startAnimation() {
        float leftPageEdgePercent = page.tx() / game.plat.graphics().viewSize.width();
        game.plat.log().debug(leftPageEdgePercent + "");

        batch = new RotateYBatch(game.plat.graphics().gl, leftPageEdgePercent, 0.5f, 1.5f);
        page.setBatch(batch);
        updateAngle(0);

        iface.anim.tween(new Animation.Value() {
            @Override
            public float initial() {
                return 0;
            }

            @Override
            public void set(float value) {
                updateAngle(value);
            }
        }).from(0).to(duration).in(duration);
    }

    private void updateAngle(float elapsed) {
        float percent = interp.applyClamp(0, 0.5f, elapsed, duration);
        batch.angle = FloatMath.PI * percent;
    }

    @Override
    public Game game() {
        return game;
    }
}
