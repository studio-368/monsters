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

import com.google.common.collect.Lists;
import edu.bsu.storygame.core.MonsterGame;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import pythagoras.f.FloatMath;
import pythagoras.f.IRectangle;
import pythagoras.f.Rectangle;
import react.RFuture;
import react.RPromise;
import react.Slot;
import tripleplay.anim.Animation;
import tripleplay.anim.Animator;
import tripleplay.shaders.RotateYBatch;

import java.util.Collection;
import java.util.List;
import java.util.Stack;

import static com.google.common.base.Preconditions.*;

public class Notebook extends GroupLayer {

    private static final float FLIP_DURATION = 1000;
    private static final float DELAY_BETWEEN_CLOSING_PAGES = 200;

    private final MonsterGame game;
    private final Animator anim;
    private final IRectangle openBounds;
    private boolean flipping = false;
    private final Stack<Page> rightPages = new Stack<>();
    private final Stack<Page> leftPages = new Stack<>();

    public Notebook(MonsterGame game, Animator anim, IRectangle openBounds, Layer... layers) {
        super(game.plat.graphics().viewSize.width(), game.plat.graphics().viewSize.height());
        checkArgument(layers.length % 2 == 1, "I demand an odd number of layers");

        this.game = game;
        this.openBounds = new Rectangle(openBounds);
        this.anim = checkNotNull(anim);

        assembleBookFrom(layers);
    }

    private void assembleBookFrom(Layer[] layers) {
        List<Page> pages = Lists.newArrayList();
        for (int i = 0; i < layers.length; i += 2) {
            Page page = (i == layers.length - 1) ? new Page(layers[i]) : new Page(layers[i], layers[i + 1]);
            pages.add(page);
        }
        for (int i = pages.size() - 1; i >= 0; i--) {
            Page page = pages.get(i);
            rightPages.push(page);
            addAtClosedPageLocation(page.front);
        }
    }

    private void addAtClosedPageLocation(Layer layer) {
        addAt(layer, openBounds.x() + openBounds.width() / 2, openBounds.y());
    }

    public RFuture<Notebook> turnPage() {
        checkState(!flipping, "Already flipping, cannot deal with it");
        checkState(rightPages.size() > 1, "Cannot turn last page");
        flipping = true;

        final RPromise<Notebook> promise = RPromise.create();
        final Page page = rightPages.pop();
        page.turnLeft().onSuccess(new Slot<Page>() {
            @Override
            public void onEmit(Page page) {
                promise.succeed(Notebook.this);
            }
        });
        return promise;
    }

    public RFuture<Notebook> closeBook() {
        checkState(!flipping, "Animation progress, cannot deal with it");
        checkState(!leftPages.isEmpty(), "Already closed");
        flipping = true;

        final RPromise<Notebook> promise = RPromise.create();
        final List<RFuture<Page>> pageClosings = Lists.newArrayList();

        int delay = 0;
        while (!leftPages.isEmpty()) {
            final RotateYBatch batch = new RotateYBatch(game.plat.graphics().gl, 0.5f, 0.5f, 1.5f);
            final Page page = leftPages.pop();
            batch.angle = FloatMath.PI;
            page.back.setBatch(batch);
            anim.delay(delay)
                    .then()
                    .action(new Runnable() {
                        @Override
                        public void run() {
                            pageClosings.add(page.turnRight());
                        }
                    });
            delay += DELAY_BETWEEN_CLOSING_PAGES;
        }
        RFuture.collect(pageClosings).onSuccess(new Slot<Collection<Page>>() {
            @Override
            public void onEmit(Collection<Page> pages) {
                flipping = false;
                promise.succeed(Notebook.this);
            }
        });
        return promise;
    }

    private final class Page {
        final Layer front;
        final Layer back;

        Page(Layer front) {
            this.front = checkNotNull(front);
            this.back = null;
        }

        Page(Layer front, Layer back) {
            checkArgument(front != back);
            this.front = checkNotNull(front);
            this.back = checkNotNull(back);
        }

        RFuture<Page> turnLeft() {
            final RPromise<Page> promise = RPromise.create();
            final RotateYBatch batch = new RotateYBatch(game.plat.graphics().gl, 0.5f, 0.5f, 1.5f);
            front.setBatch(batch);

            BatchAngleAnimator batchAngle = new BatchAngleAnimator(batch);
            anim.tween(batchAngle)
                    .from(0)
                    .to(FloatMath.HALF_PI)
                    .in(FLIP_DURATION / 2)
                    .easeIn()
                    .then()
                    .action(new Runnable() {
                        @Override
                        public void run() {
                            remove(front);
                            addAtClosedPageLocation(back);
                            back.setBatch(batch);
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
                            flipping = false;
                            leftPages.push(Page.this);
                            promise.succeed(Page.this);
                        }
                    });
            return promise;
        }

        RFuture<Page> turnRight() {
            final RPromise<Page> promise = RPromise.create();
            final RotateYBatch batch = new RotateYBatch(game.plat.graphics().gl, 0.5f, 0.5f, 1.5f);
            batch.angle = FloatMath.PI;
            back.setBatch(batch);
            BatchAngleAnimator batchAngle = new BatchAngleAnimator(batch);
            anim.tween(batchAngle)
                    .from(FloatMath.PI)
                    .to(FloatMath.HALF_PI)
                    .in(FLIP_DURATION / 2)
                    .easeIn()
                    .then()
                    .action(new Runnable() {
                        @Override
                        public void run() {
                            remove(back);
                            addAtClosedPageLocation(front);
                            front.setBatch(batch);
                        }
                    })
                    .then()
                    .tween(batchAngle)
                    .from(FloatMath.HALF_PI)
                    .to(0)
                    .in(FLIP_DURATION / 2)
                    .easeOut()
                    .then()
                    .action(new Runnable() {
                        @Override
                        public void run() {
                            rightPages.push(Page.this);
                            promise.succeed(Page.this);
                        }
                    });
            return promise;
        }
    }

    private final class BatchAngleAnimator implements Animation.Value {

        private final RotateYBatch batch;

        BatchAngleAnimator(RotateYBatch batch) {
            this.batch = checkNotNull(batch);
        }

        @Override
        public float initial() {
            return 0;
        }

        @Override
        public void set(float value) {
            batch.angle = value;
        }
    }

}
