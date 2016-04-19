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

package edu.bsu.storygame.core.view;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Phase;
import edu.bsu.storygame.core.model.Player;
import edu.bsu.storygame.core.model.Region;
import playn.core.*;
import playn.scene.ImageLayer;
import pythagoras.f.IPoint;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;
import react.Signal;
import react.SignalView;
import react.Slot;
import tripleplay.anim.Animation;
import tripleplay.anim.Animator;
import tripleplay.util.Colors;
import tripleplay.util.Layers;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public final class MapView extends ImageLayer {

    /**
     * The mapping of rectangular regions in the unscaled source image to their regions.
     */
    private static final Map<Rectangle, Region> NATURAL_COORDINATE_MAP = ImmutableMap.<Rectangle, Region>builder()
            .put(new Rectangle(496, 226, 186, 290), Region.AFRICA)
            .put(new Rectangle(682, 69, 299, 232), Region.ASIA)
            .put(new Rectangle(906, 370, 220, 194), Region.OCEANIA)
            .put(new Rectangle(513, 91, 123, 101), Region.EUROPE)
            .put(new Rectangle(117, 78, 266, 214), Region.NORTH_AMERICA)
            .put(new Rectangle(117, 300, 300, 214), Region.SOUTH_AMERICA)
            .build();

    /**
     * Pin locations in "natural" unscaled coordinates
     */
    private static final Map<Region, IPoint> NATURAL_PIN_LOCATIONS = ImmutableMap.<Region, IPoint>builder()
            .put(Region.AFRICA, new Point(611, 307))
            .put(Region.ASIA, new Point(879, 158))
            .put(Region.OCEANIA, new Point(973, 425))
            .put(Region.EUROPE, new Point(568, 111))
            .put(Region.NORTH_AMERICA, new Point(196, 137))
            .put(Region.SOUTH_AMERICA, new Point(327, 369))
            .build();

    public final SignalView<Region> pick = Signal.create();
    private final GameContext context;
    private final Animator anim;
    private final ImmutableList<Pin> pins;

    public MapView(GameContext context, final Animator anim) {
        super(context.game.imageCache.image(ImageCache.Key.BACKGROUND));
        this.context = context;
        this.anim = checkNotNull(anim);
        configureMapImagePointer();
        this.pins = makePinList();
    }

    private void configureMapImagePointer() {
        context.game.pointer.events.connect(new Slot<Pointer.Event>() {
            private final Point local = new Point();
            @Override
            public void onEmit(Pointer.Event event) {
                if (event.kind.isEnd && context.phase.get().equals(Phase.MOVEMENT)) {
                    Layers.transform(new Point(event.x, event.y), context.game.rootLayer, MapView.this, local);
                    for (Rectangle r : NATURAL_COORDINATE_MAP.keySet()) {
                        if (r.contains(local.x, local.y)) {
                            ((Signal<Region>) pick).emit(NATURAL_COORDINATE_MAP.get(r));
                        }
                    }
                }
            }
        });
    }

    private ImmutableList<Pin> makePinList() {
        final float offset = context.game.bounds.percentOfHeight(0.06f);
        ImmutableList.Builder<Pin> builder = new ImmutableList.Builder<>();
        for (int i = 0, limit = context.players.size(); i < limit; i++) {
            Player p = context.players.get(i);
            Pin pin = new Pin(p).offset(i * offset);
            builder.add(pin);
        }
        return builder.build();
    }

    @Override
    protected void paintImpl(Surface surf) {
        super.paintImpl(surf);
        for (int i = 0, limit = pins.size(); i < limit; i++) {
            pins.get(i).paint(surf);
        }
    }

    private final class Pin {
        private static final float PIN_RADIUS_PERCENT = 0.02f;
        private static final float ANIMATION_DURATION = 350f;

        private final Point location = new Point(0, 0);
        private final Texture texture;
        private float offset = 0;

        private Pin(Player player) {
            Region playerStartLocation = checkNotNull(player.location.get());
            location.set(NATURAL_PIN_LOCATIONS.get(playerStartLocation));
            texture = createPinTexture(player.name);
            player.location.connect(new Slot<Region>() {
                private final Animation.XYValue locationAdapter = new Animation.XYValue() {
                    @Override
                    public float initialX() {
                        return location.x;
                    }

                    @Override
                    public float initialY() {
                        return location.y;
                    }

                    @Override
                    public void set(float x, float y) {
                        location.set(x, y);
                    }
                };

                @Override
                public void onEmit(Region region) {
                    IPoint destination = NATURAL_PIN_LOCATIONS.get(region);
                    anim.tween(locationAdapter)
                            .to(destination.x(), destination.y())
                            .in(ANIMATION_DURATION)
                            .easeInOut();
                }
            });
        }

        private Texture createPinTexture(String name) {
            final float radius = context.game.bounds.percentOfHeight(PIN_RADIUS_PERCENT);
            Canvas canvas = context.game.plat.graphics().createCanvas(100, radius * 2);
            canvas.setFillColor(Colors.WHITE);
            canvas.fillCircle(radius, canvas.height / 2, canvas.height / 2);
            Font font = Typeface.GAME_TEXT.font.derive(radius * 1.5f); // Leave room for descenders!
            TextFormat format = new TextFormat(font);
            TextLayout layout = context.game.plat.graphics().layoutText(name, format);
            canvas.fillText(layout, radius * 2, 0);
            return canvas.toTexture();
        }

        public Pin offset(float offset) {
            this.offset = offset;
            return this;
        }

        public void paint(Surface surf) {
            surf.draw(texture, location.x, location.y + offset);
        }
    }
}
