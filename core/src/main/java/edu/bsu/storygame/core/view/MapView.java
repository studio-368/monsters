package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Phase;
import react.*;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class MapView extends Group {

    private final GameContext context;
    private final UnitSignal onLeftHemisphere = new UnitSignal();
    private final UnitSignal onRightHemisphere = new UnitSignal();

    public MapView(GameContext gameContext) {
        super(AxisLayout.horizontal());
        this.context = checkNotNull(gameContext);
        add(new RegionButton(ImageCache.Key.MAP_LEFT),
                new RegionButton(ImageCache.Key.MAP_RIGHT));
    }

    private final class RegionButton extends Button {
        RegionButton(ImageCache.Key key) {
            this.icon.update(Icons.image(context.game.imageCache.image(key)));
            context.phase.connect(new Slot<Phase>() {
                @Override
                public void onEmit(Phase phase) {
                    setEnabled(phase.equals(Phase.MOVEMENT));
                }
            });
            onClick(new SignalView.Listener<Button>() {
                @Override
                public void onEmit(Button button) {
                    context.phase.update(Phase.ENCOUNTER);
                }
            });
        }
    }

}
