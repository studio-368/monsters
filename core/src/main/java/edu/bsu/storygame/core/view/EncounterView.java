package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Encounter;
import edu.bsu.storygame.core.model.Phase;
import edu.bsu.storygame.core.model.Reaction;
import react.Slot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class EncounterView extends Group {
    private final GameContext context;

    public EncounterView(final GameContext context, Encounter encounter) {
        super(AxisLayout.vertical());
        this.context = checkNotNull(context);
        add(new Label(encounter.name, Icons.scaled(Icons.image(encounter.image), 0.25f))
                .setStyles(Style.ICON_POS.above));
        Group reactionBox = new Group(AxisLayout.horizontal());
        for (final Reaction reaction : encounter.reactions) {
            reactionBox.add(new ReactionButton(reaction)
                    .onClick(new Slot<Button>() {
                        @Override
                        public void onEmit(Button button) {
                            context.phase.update(Phase.STORY);
                            add(new ExchangeScreen(context, reaction.story));
                        }
                    }));
        }
        add(reactionBox);
    }

    private final class ReactionButton extends Button {
        private ReactionButton(Reaction reaction) {
            super(reaction.name);
            context.phase.connect(new Slot<Phase>() {
                @Override
                public void onEmit(Phase phase) {
                    updateEnabledStatusBasedOn(phase);
                }
            });
            updateEnabledStatusBasedOn(context.phase.get());
        }
        private void updateEnabledStatusBasedOn(Phase phase) {
            setEnabled(phase.equals(Phase.ENCOUNTER));
        }
    }
}
