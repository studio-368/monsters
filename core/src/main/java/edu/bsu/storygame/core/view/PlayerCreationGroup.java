package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.model.Skill;
import react.Slot;
import react.Value;
import tripleplay.ui.*;
import tripleplay.ui.layout.TableLayout;

import java.util.Set;

public class PlayerCreationGroup extends Group {

    public Value<Boolean> isFilled = Value.create(false);
    private MonsterGame game;
    public BiSelector selector = new BiSelector();
    private Group skillGroup;
    public Field nameField;

    public PlayerCreationGroup(Layout layout, MonsterGame game) {
        super(layout);
        this.game = game;
        add(nameField = initNameField());
        add(skillGroup = createSkillGroup());
        linkSelector();

        nameField.text.connect(new Slot<String>() {
            @Override
            public void onEmit(String s) {
                if (checkContinueProtocol()) {
                    isFilled.update(true);
                } else {
                    isFilled.update(false);
                }
            }
        });
    }

    private Field initNameField() {
        return new Field("")
                .setConstraint(Constraints.fixedSize(game.bounds.width() * 0.15f, game.bounds.height() * 0.08f));
    }

    private void linkSelector() {
        for (Element<?> button : skillGroup) {
            selector.add((ToggleButton) button);
            ((ToggleButton) button).selected().connect(new Slot<Boolean>() {
                @Override
                public void onEmit(Boolean isSelected) {
                    if (checkContinueProtocol()) {
                        isFilled.update(true);
                    } else {
                        isFilled.update(false);
                    }
                }
            });
        }

    }

    private Group createSkillGroup() {
        Set<Skill> skillSet = game.narrativeCache.state.result().get().skills();
        Group group = new Group(new TableLayout(2).gaps(20, 20));
        for (Skill skill : skillSet) {
            group.add(new SkillButton(skill));

        }
        return group;
    }


    private Boolean checkContinueProtocol() {
        return selector.selections().size() == 2 && !nameField.text.get().equals("");
    }

    private final class SkillButton extends ToggleButton {

        private static final float PERCENT_OF_HEIGHT = 0.1f;
        private static final float PERCENT_OF_WIDTH = 0.2f;

        SkillButton(Skill skill) {
            super(skill.name);
            setConstraint(Constraints.fixedSize(
                    game.bounds.width() * PERCENT_OF_WIDTH,
                    game.bounds.height() * PERCENT_OF_HEIGHT));
        }

    }

}


