package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Story;
import react.SignalView;
import tripleplay.ui.Button;
import tripleplay.ui.Constraints;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.layout.AxisLayout;

public class ExchangeScreen extends Group{
    private final GameContext context;
    private final Story story;

    public ExchangeScreen(final GameContext context, Story story){
        super(AxisLayout.vertical());
        this.context = context;
        this.story = story;
        add(new Label("Pass to next player"));
        add(new PassButton());
    }

    private final class PassButton extends Button {
        private static final float PERCENT_OF_HEIGHT = 0.1f;
        private static final float PERCENT_OF_WIDTH = 0.2f;

        PassButton() {
            super("READY");
            setConstraint(Constraints.fixedSize(
                    context.game.bounds.width() * PERCENT_OF_WIDTH,
                    context.game.bounds.height() * PERCENT_OF_HEIGHT));
            onClick(new SignalView.Listener<Button>() {
                @Override
                public void onEmit(Button button) {
                    add(new StoryView(context, story));
                }
            });
        }
    }
}
