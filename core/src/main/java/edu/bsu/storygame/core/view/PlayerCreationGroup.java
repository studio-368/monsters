package edu.bsu.storygame.core.view;

import com.google.common.collect.Lists;
import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.model.Region;
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
    private final BiSelector biselector = new BiSelector();
    private final Selector selector = new Selector();

    public PlayerCreationGroup(MonsterGame game) {
        super(AxisLayout.vertical().offStretch());
        this.game = checkNotNull(game);
        add(new StyledLabel("Choose two skills:").addStyles(Style.HALIGN.left),
                createRegionGroup());
        add(new StyledLabel("Choose two skills:").addStyles(Style.HALIGN.left),
                createSkillGroup());
        watchForFormCompletion();
    }

    private void watchForFormCompletion() {
        selector.selected.connect(new Slot<Element<?>>() {
            @Override
            public void onEmit(Element<?> element) {
                checkForCompletion();
            }
        });
        biselector.selections.connect(new RList.Listener<ToggleButton>() {
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
        final boolean isComplete = biselector.size() == NUMBER_OF_SKILLS
                && ((RegionButton)selector.selected.get()).region != null;
        ((Value<Boolean>) complete).update(isComplete);
    }

    private Group createSkillGroup() {
        Set<Skill> skillSet = game.narrativeCache.state.result().get().skills();
        Group group = new Group(new FlowLayout());
        for (Skill skill : skillSet) {
            SkillButton skillButton = new SkillButton(skill);
            biselector.add(skillButton);
            group.add(skillButton);
        }
        return group;
    }

    private Group createRegionGroup(){
        Group group = new Group(new FlowLayout());
        for(Region region : Region.values()){
            RegionButton regionButton = new RegionButton(region);
            selector.add(regionButton);
            group.add(regionButton);
        }
        return group;
    }

    public List<Skill> getSelectedSkills() {
        List<Skill> result = Lists.newArrayList();
        for (ToggleButton button : biselector.selections()) {
            Skill skill = ((SkillButton) button).skill;
            result.add(skill);
        }
        return result;
    }


    final class SkillButton extends ToggleButton {

        private static final float PERCENT_OF_WIDTH = 0.22f;

        final Skill skill;

        SkillButton(Skill skill) {
            super(skill.name);
            this.skill = skill;
            setConstraint(Constraints.fixedWidth(
                    game.bounds.width() * PERCENT_OF_WIDTH));
        }

        @Override
        protected Class<?> getStyleClass() {
            return SkillButton.class;
        }
    }

    final class RegionButton extends ToggleButton {
        private static final float PERCENT_OF_WIDTH = 0.22f;

        final Region region;

        RegionButton(Region region) {
            super(region.name().toLowerCase().replace("_", " "));
            this.region = region;
            setConstraint(Constraints.fixedWidth(
                    game.bounds.width() * PERCENT_OF_WIDTH));
        }

        @Override
        protected Class<?> getStyleClass() {
            return SkillButton.class;
        }
    }
}


