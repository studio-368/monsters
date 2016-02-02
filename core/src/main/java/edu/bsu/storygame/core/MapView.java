package edu.bsu.storygame.core;

import playn.scene.Pointer;
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
        add(new RegionButton("images/map-left.png"),
                new RegionButton("images/map-right.png"));
    }

    private final class RegionButton extends Button {
        RegionButton(String imagePath) {
            this.icon.update(Icons.image(context.game.plat.assets().getImage(imagePath)));
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
