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
import pythagoras.f.Rectangle;
import react.RFuture;
import react.RPromise;
import tripleplay.anim.Animation;
import tripleplay.anim.Animator;
import tripleplay.shaders.RotateYBatch;

import static com.google.common.base.Preconditions.*;

public class Notebook extends GroupLayer {

    private static final float FLIP_DURATION = 1000;

    private final MonsterGame game;
    private final Animator anim;
    private ImmutableList<Layer> layers;
    private RotateYBatch batch;
    private final IRectangle openBounds;
    private int rightTopPageIndex = 0;
    private boolean flipping = false;

    public Notebook(MonsterGame game, Animator anim, IRectangle openBounds, Layer... layers) {
        super(game.plat.graphics().viewSize.width(), game.plat.graphics().viewSize.height());
        checkArgument(layers.length % 2 == 1, "I demand an odd number of layers");

        this.game = game;
        this.openBounds = new Rectangle(openBounds);
        this.anim = checkNotNull(anim);
        this.layers = ImmutableList.copyOf(layers);

        for (int i = layers.length - 1; i >= 0; i -= 2) {
            addAt(layers[i], openBounds.x() + openBounds.width() / 2, openBounds.y());
        }
    }

    public RFuture<Notebook> turnPage() {
        if (flipping) {
            throw new IllegalStateException("Cannot run two animations concurrently");
        } else {
            flipping = true;
        }
        checkState(rightTopPageIndex < layers.size() - 1, "On last page, can not flip any more.");

        final RPromise<Notebook> promise = RPromise.create();
        final Layer page = layers.get(rightTopPageIndex);
        final Layer reverse = layers.get(rightTopPageIndex + 1);
        batch = new RotateYBatch(game.plat.graphics().gl, 0.5f, 0.5f, 1.5f);
        page.setBatch(batch);

        anim.tween(batchAngle)
                .from(0)
                .to(FloatMath.HALF_PI)
                .in(FLIP_DURATION / 2)
                .easeIn()
                .then()
                .action(new Runnable() {
                    @Override
                    public void run() {
                        remove(page);
                        addAt(reverse, openBounds.x() + openBounds.width() / 2, openBounds.y());
                        reverse.setBatch(batch);
                    }
                })
                .then()
                .tween(batchAngle)
                .from(FloatMath.HALF_PI)
                .to(FloatMath.PI)
                .in(FLIP_DURATION / 2)
                .easeOut()
                .then()
                .action(new Runnable() {
                    @Override
                    public void run() {
                        rightTopPageIndex += 2;
                        flipping = false;
                        promise.succeed(Notebook.this);
                    }
                });
        return promise;
    }

    private final Animation.Value batchAngle = new Animation.Value() {
        @Override
        public float initial() {
            return 0;
        }

        @Override
        public void set(float value) {
            batch.angle = value;
        }
    };
}
