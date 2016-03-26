package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Region;
import edu.bsu.storygame.core.util.ScalablePicker;
import playn.core.Pointer;
import playn.scene.ImageLayer;
import playn.scene.Layer;
import pythagoras.f.Rectangle;
import react.Signal;
import react.SignalView;
import react.Slot;

public final class MapView extends ImageLayer {

    public final SignalView<Region> pick = Signal.create();

    private final GameContext context;
    private final ScalablePicker<Region> picker = new ScalablePicker<>();

    public MapView(GameContext context) {
        super(context.game.imageCache.image(ImageCache.Key.BACKGROUND));
        this.context = context;
        configureMapImagePointer();
        registerPickableRectangles();
    }

    private void configureMapImagePointer() {
        context.game.pointer.events.connect(new Slot<Pointer.Event>() {
            @Override
            public void onEmit(Pointer.Event event) {
                if (event.kind.isEnd) {
                    Region picked = picker.pick(event.x, event.y);
                    if (picked != null) {
                        ((Signal<Region>) pick).emit(picked);
                    }
                }
            }
        });
    }

    private void registerPickableRectangles() {
        picker.register(new Rectangle(496, 226, 186, 290), Region.AFRICA);
        picker.register(new Rectangle(682, 69, 299, 232), Region.ASIA);
        picker.register(new Rectangle(906, 370, 220, 194), Region.OCEANIA);
        picker.register(new Rectangle(513, 91, 123, 101), Region.EUROPE);
        picker.register(new Rectangle(117, 78, 266, 528), Region.AMERICAS);
    }

    @Override
    public Layer setScale(float scale) {
        super.setScale(scale);
        picker.scale(scale);
        return this;
    }
}
