package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.model.Encounter;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Phase;
import edu.bsu.storygame.core.model.Region;
import react.SignalView;
import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.layout.AxisLayout;

import static com.google.common.base.Preconditions.*;

public class MapView extends Group {

    private final GameContext context;

    public MapView(GameContext gameContext) {
        super(AxisLayout.vertical());
        this.context = checkNotNull(gameContext);
        add(new RegionButton(Region.AFRICA),
                new RegionButton(Region.AMERICAS),
                new RegionButton(Region.ASIA),
                new RegionButton(Region.EUROPE),
                new RegionButton(Region.OCEANIA));
    }

    private final class RegionButton extends Button {
        private static final float PERCENT_OF_HEIGHT = 0.1f;
        private static final float PERCENT_OF_WIDTH = 0.2f;

        RegionButton(final Region region) {
            super(region.toString());
            context.phase.connect(new Slot<Phase>() {
                @Override
                public void onEmit(Phase phase) {
                    setEnabled(phase.equals(Phase.MOVEMENT));
                }
            });
            onClick(new SignalView.Listener<Button>() {
                @Override
                public void onEmit(Button button) {
                    context.currentPlayer.get().location.update(region);
                    Encounter encounter = context.game.narrativeCache.state.result().get().forRegion(region).chooseOne();
                    context.encounter.update(encounter);
                    context.phase.update(Phase.ENCOUNTER);
                }
            });
        }
    }

}
