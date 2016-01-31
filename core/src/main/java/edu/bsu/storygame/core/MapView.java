package edu.bsu.storygame.core;

import react.SignalView;
import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.layout.AxisLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class MapView extends Group {

    private final GameContext context;

    public MapView(GameContext gameContext) {
        super(AxisLayout.vertical());
        this.context = checkNotNull(gameContext);
        add(new Label("Game Map"),
                new Group(AxisLayout.horizontal())
                        .add(new RegionButton("Region 0"),
                                new RegionButton("Region 1")));
    }

    private final class RegionButton extends Button {

        public RegionButton(String text) {
            super(text);
            updateEnabledStatus();
            context.phase.connect(new Slot<Phase>() {
                @Override
                public void onEmit(Phase phase) {
                    updateEnabledStatus();
                }
            });
            onClick(new SignalView.Listener<Button>() {
                @Override
                public void onEmit(Button button) {
                    context.phase.update(context.phase.get().next());
                }
            });
        }


        private void updateEnabledStatus() {
            setEnabled(context.phase.get().equals(Phase.MOVEMENT));
        }
    }
}
