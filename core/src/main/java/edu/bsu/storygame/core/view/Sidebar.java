package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import edu.bsu.storygame.core.model.Skill;
import react.RList;
import react.SignalView;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;

import static com.google.common.base.Preconditions.checkNotNull;

public class Sidebar extends Group {

    private final GameContext context;

    public Sidebar(GameContext context) {
        super(AxisLayout.vertical().offStretch().stretchByDefault());
        this.context = checkNotNull(context);
        addStyles(Style.BACKGROUND.is(Background.solid(Colors.BLACK)));
        for (Player p : context.players) {
            add(new PlayerView(p));
        }
    }

    final class PlayerView extends Group {

        private final SkillGroup skillGroup = new SkillGroup(AxisLayout.vertical());
        private PointLabel pointLabel;
        private final Player player;

        PlayerView(Player player) {
            super(AxisLayout.vertical());
            this.player = checkNotNull(player);
            pointLabel = new PointLabel(player.storyPoints.get());
            addStyles(Style.BACKGROUND.is(Background.solid(player.color)),
                    Style.HALIGN.left,
                    Style.VALIGN.top);
            add(new Label(player.name)
                    .addStyles(Style.FONT.is(Typeface.OXYGEN.in(context.game).atSize(0.04f))));
            watchForSkillChanges();
            watchForPointChange();
            add(skillGroup);
            add(pointLabel);
            initPlayerFields();
        }

        private void initPlayerFields() {
            pointLabel.updatePlayerPoints(player.storyPoints.get());
            skillGroup.updatePlayerSkills(player);
        }

        private void watchForSkillChanges() {
            player.skills.connect(new RList.Listener<Skill>() {
                @Override
                public void onAdd(Skill elem) {
                    skillGroup.updatePlayerSkills(player);

                }

                @Override
                public void onRemove(Skill elem) {
                    skillGroup.updatePlayerSkills(player);
                }
            });
        }

        private void watchForPointChange() {
            player.storyPoints.connect(new SignalView.Listener<Integer>() {
                @Override
                public void onEmit(Integer integer) {
                    pointLabel.updatePlayerPoints(integer);
                }
            });
        }
    }

    final class SkillLabel extends Label {
        private SkillLabel(Skill skill) {
            super(skill.name);
        }

        @Override
        protected Class<?> getStyleClass() {
            return SkillLabel.class;
        }
    }

    final class SkillGroup extends Group {
        private SkillGroup(Layout layout) {
            super(layout);
        }

        private void updatePlayerSkills(Player player) {
            this.removeAll();
            for (Skill skill : player.skills) {
                this.add(new SkillLabel(skill));
            }
        }
    }

    final class PointLabel extends Label {
        private PointLabel(Integer points) {
            super(points.toString());
        }

        private void updatePlayerPoints(Integer points) {
            this.setText(points.toString());
        }
    }

}
