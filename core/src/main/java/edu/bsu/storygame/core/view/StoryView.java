package edu.bsu.storygame.core.view;

import com.google.common.collect.Lists;
import edu.bsu.storygame.core.model.*;
import react.Slot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

import java.util.List;

public class StoryView extends Group {
    public StoryView(final GameContext context, Story story) {
        super(AxisLayout.vertical());
        add(new Label(story.text)
                .addStyles(Style.TEXT_WRAP.on));
        Group triggerBox = new Group(AxisLayout.horizontal());

        final List<Button> buttons = Lists.newArrayList();
        for (final SkillTrigger trigger : story.triggers) {
            Button button = new Button(trigger.skill.text)
                    .onClick(new Slot<Button>() {
                        @Override
                        public void onEmit(Button button) {
                            for (Button b : buttons) {
                                b.setEnabled(false);
                            }
                            add(new Label(trigger.story)
                                    .addStyles(Style.TEXT_WRAP.on));
                            add(new Button("OK")
                                    .onClick(new Slot<Button>() {
                                        @Override
                                        public void onEmit(Button button) {
                                            context.phase.update(Phase.END_OF_ROUND);
                                        }
                                    }));
                        }
                    });
            buttons.add(button);
            triggerBox.add(button);
        }
        add(triggerBox);
    }
}
