package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import react.RList;
import tripleplay.ui.Background;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
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
        private final Group skillGroup = new Group(AxisLayout.vertical());
        private final Player player;

        PlayerView(Player player) {
            super(AxisLayout.vertical());
            this.player = checkNotNull(player);
            addStyles(Style.BACKGROUND.is(Background.solid(player.color)),
                    Style.HALIGN.left,
                    Style.VALIGN.top);
            add(new Label(player.name)
                    .addStyles(Style.FONT.is(Typeface.OXYGEN.in(context.game).atSize(0.04f))));
            watchForSkillChanges();
            add(skillGroup);
            regenerateSkillGroup();
        }

        private void watchForSkillChanges() {
            player.skills.connect(new RList.Listener<String>() {
                @Override
                public void onAdd(String elem) {
                    regenerateSkillGroup();
                }

                @Override
                public void onRemove(String elem) {
                    regenerateSkillGroup();
                }
            });
        }

        private void regenerateSkillGroup() {
            skillGroup.removeAll();
            for (String skill : player.skills) {
                skillGroup.add(new SkillLabel(skill));
            }
        }
    }

    final class SkillLabel extends Label {
        private SkillLabel(String skill) {
            super(skill);
        }

        @Override
        protected Class<?> getStyleClass() {
            return SkillLabel.class;
        }
    }
}
