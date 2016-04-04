package edu.bsu.storygame.core.view;

import com.google.common.collect.ImmutableMap;
import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Phase;
import edu.bsu.storygame.core.model.Region;
import playn.core.Pointer;
import playn.scene.ImageLayer;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;
import react.Signal;
import react.SignalView;
import react.Slot;
import tripleplay.util.Layers;

import java.util.Map;

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

    public final SignalView<Region> pick = Signal.create();
    private final GameContext context;

    public MapView(GameContext context) {
        super(context.game.imageCache.image(ImageCache.Key.BACKGROUND));
        this.context = context;
        configureMapImagePointer();
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

}
