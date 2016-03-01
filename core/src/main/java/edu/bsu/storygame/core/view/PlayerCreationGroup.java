package edu.bsu.storygame.core.view;

import com.google.common.collect.Lists;
import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.model.Player;
import edu.bsu.storygame.core.model.Skill;
import react.RList;
import react.Slot;
import react.Value;
import react.ValueView;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.FlowLayout;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public final class PlayerCreationGroup extends Group {

    private static final int NUMBER_OF_SKILLS = 2;

    public final ValueView<Boolean> complete = Value.create(false);

    private final MonsterGame game;
    private final BiSelector selector = new BiSelector();
    private Field nameField;

    public PlayerCreationGroup(MonsterGame game) {
        super(AxisLayout.vertical().offStretch());
        this.game = checkNotNull(game);
        add(createNameArea(),
                new Label("Choose two skills:").addStyles(Style.HALIGN.left),
                createSkillGroup());
        watchForFormCompletion();
    }

    private Group createNameArea() {
        Group group = new Group(AxisLayout.horizontal());
        group.add(new Label("Name:"));
        group.add(nameField = new Field()
                .setConstraint(Constraints.fixedSize(game.bounds.width() * 0.15f, game.bounds.height() * 0.08f)));
        group.addStyles(Style.BACKGROUND.is(Background.blank().inset(game.bounds.percentOfHeight(0.02f))));
        return group;
    }

    private void watchForFormCompletion() {
        nameField.text.connect(new Slot<String>() {
            @Override
            public void onEmit(String s) {
                checkForCompletion();
            }
        });

        selector.selections.connect(new RList.Listener<ToggleButton>() {
            @Override
            public void onAdd(ToggleButton elem) {
                checkForCompletion();
            }

            @Override
            public void onRemove(ToggleButton elem) {
                checkForCompletion();
            }
        });
    }

    private void checkForCompletion() {
        final boolean isComplete = selector.size() == NUMBER_OF_SKILLS
                && !nameField.text.get().trim().isEmpty();
        ((Value<Boolean>) complete).update(isComplete);
    }

    private Group createSkillGroup() {
        Set<Skill> skillSet = game.narrativeCache.state.result().get().skills();
        Group group = new Group(new FlowLayout());
        for (Skill skill : skillSet) {
            SkillButton skillButton = new SkillButton(skill);
            selector.add(skillButton);
            group.add(skillButton);
        }
        return group;
    }

    public Player.Builder createPlayerBuilder() {
        return new Player.Builder()
                .name(nameField.text.get().trim())
                .skills(getSelectedSkills());
    }

    private List<Skill> getSelectedSkills() {
        List<Skill> result = Lists.newArrayList();
        for (ToggleButton button : selector.selections()) {
            Skill skill = ((SkillButton) button).skill;
            result.add(skill);
        }
        return result;
    }

    final class SkillButton extends ToggleButton {

        private static final float PERCENT_OF_HEIGHT = 0.10f;
        private static final float PERCENT_OF_WIDTH = 0.20f;

        final Skill skill;

        SkillButton(Skill skill) {
            super(skill.name);
            this.skill = skill;
            setConstraint(Constraints.fixedSize(
                    game.bounds.width() * PERCENT_OF_WIDTH,
                    game.bounds.height() * PERCENT_OF_HEIGHT));
        }

        @Override
        protected Class<?> getStyleClass() {
            return SkillButton.class;
        }
    }

}


