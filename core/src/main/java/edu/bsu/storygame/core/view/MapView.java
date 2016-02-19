package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Phase;
import edu.bsu.storygame.core.model.Region;
import pythagoras.f.IDimension;
import react.SignalView;
import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Constraints;
import tripleplay.ui.Group;
import tripleplay.ui.layout.AxisLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class MapView extends Group {

    private final GameContext context;

    public MapView(GameContext gameContext, IDimension size) {
        super(AxisLayout.horizontal());
        this.context = checkNotNull(gameContext);
        setConstraint(Constraints.fixedSize(size.width(), size.height()));
        add(new RegionButton(Region.AMERICAS),
                new RegionButton(Region.ASAIA));
    }

    private final class RegionButton extends Button {
        private static final float PERCENT_OF_HEIGHT = 0.8f;
        private static final float PERCENT_OF_WIDTH = 0.4f;

        RegionButton(final Region region) {
            super();
            setConstraint(Constraints.fixedSize(
                    context.game.bounds.width() * PERCENT_OF_WIDTH,
                    context.game.bounds.height() * PERCENT_OF_HEIGHT));
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
                    context.currentPlayer.get().location.update(region);
                }
            });
        }
    }

}
