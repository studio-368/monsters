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

package edu.bsu.storygame.core.view;

import com.google.common.collect.ImmutableList;
import edu.bsu.storygame.core.MonsterGame;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import pythagoras.f.FloatMath;
import pythagoras.f.IRectangle;
import tripleplay.anim.Animation;
import tripleplay.anim.Animator;
import tripleplay.shaders.RotateYBatch;
import tripleplay.util.Interpolator;

import static com.google.common.base.Preconditions.checkNotNull;

public class Notebook extends GroupLayer {

    private final MonsterGame game;
    private final Animator anim;
    private ImmutableList<Layer> layers;
    private RotateYBatch batch;
    private final Interpolator interp = Interpolator.EASE_INOUT;
    private final float duration = 2000;

    public Notebook(MonsterGame game, Animator anim, IRectangle openBounds, Layer... layers) {
        super(game.plat.graphics().viewSize.width(), game.plat.graphics().viewSize.height());
        this.game = game;
        this.anim = checkNotNull(anim);
        this.layers = ImmutableList.copyOf(layers);

        for (int i=layers.length-1; i>=0; i--) {
            addAt(layers[i], openBounds.x() + openBounds.width() / 2, openBounds.y());
        }
    }

    public void turnPage() {
        startAnimation();
    }

    private void startAnimation() {
        final Layer page = layers.get(0);

        batch = new RotateYBatch(game.plat.graphics().gl, 0.5f, 0.5f, 1.5f);
        page.setBatch(batch);
        updateAngle(0);

        anim.tween(new Animation.Value() {
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
}
