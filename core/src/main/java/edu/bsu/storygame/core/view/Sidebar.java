package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import edu.bsu.storygame.core.model.Skill;
import edu.bsu.storygame.core.util.IconScaler;
import react.RList;
import react.SignalView;
import react.Slot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Sidebar extends Group {

    private final GameContext context;

    public Sidebar(GameContext context) {
        super(AxisLayout.vertical().offStretch().stretchByDefault());
        this.context = checkNotNull(context);
        addPlayerViews();
    }

    private void addPlayerViews() {
        add(new PlayerView(0));
        add(new PlayerView(1));
    }

    final class PlayerView extends Group {

        private final SkillGroup skillGroup;
        private PointLabel pointLabel;
        private final Player player;

        PlayerView(int playerNumber) {
            super(AxisLayout.horizontal());
            this.player = context.players.get(playerNumber);
            Icon star = new IconScaler(context.game)
                    .scale(ImageCache.Key.STAR, context.game.bounds.width() * 0.08f);

            Group textGroup = new Group(AxisLayout.vertical().offStretch())
                    .add(new NameLabel(player))
                    .add(skillGroup = new SkillGroup(playerNumber == 0 ? Palette.SPROUT : Palette.BLACK_PEARL))
                    .add(new Shim(1f, 50f))
                    .add(new TurnLabel(star))
                    .setConstraint(AxisLayout.stretched());

            pointLabel = new PointLabel(player.storyPoints.get());

            add(textGroup,
                    pointLabel);

            addStyles(Style.BACKGROUND.is(Background.solid(player.color).inset(context.game.bounds.percentOfHeight(0.01f))),
                    Style.HALIGN.left,
                    Style.VALIGN.top);

            watchForSkillChanges();
            watchForPointChange();
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

        final class TurnLabel extends Label {
            private TurnLabel(Icon icon) {
                super(icon);
                context.currentPlayer.connect(new Slot<Player>() {
                    @Override
                    public void onEmit(Player player) {
                        setVisible(player == PlayerView.this.player);
                    }
                });
                setVisible(context.currentPlayer.get() == PlayerView.this.player);
            }

            @Override
            protected Class<?> getStyleClass() {
                return TurnLabel.class;
            }
        }

    }


    final class NameLabel extends Label {
        private NameLabel(Player p) {
            super(p.name);
        }

        @Override
        protected Class<?> getStyleClass() {
            return NameLabel.class;
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

        private final int textColor;

        private SkillGroup(int textColor) {
            super(AxisLayout.vertical().offStretch());
            this.textColor = textColor;
        }

        private void updatePlayerSkills(Player player) {
            this.removeAll();
            for (Skill skill : player.skills) {
                this.add(new SkillLabel(skill).addStyles(Style.COLOR.is(textColor)));
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

        @Override
        protected Class<?> getStyleClass() {
            return PointLabel.class;
        }
    }
}
