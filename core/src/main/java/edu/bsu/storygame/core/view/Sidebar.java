/*
 * Copyright 2016 Paul Gestwicki
 *
 * This file is part of Traveler's Notebook: Monster Tales
 *
 * Traveler's Notebook: Monster Tales is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Traveler's Notebook: Monster Tales is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Traveler's Notebook: Monster Tales.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import edu.bsu.storygame.core.model.Skill;
import edu.bsu.storygame.core.util.IconScaler;
import react.RList;
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
        private final Player player;

        PlayerView(int playerNumber) {
            super(AxisLayout.horizontal());
            this.player = context.players.get(playerNumber);

            Icon star = new IconScaler(context.game)
                    .scale(ImageCache.Key.STAR, context.game.bounds.width() * 0.08f);

            Group textGroup = new Group(AxisLayout.vertical().offStretch())
                    .add(new NameLabel(player))
                    .add(skillGroup = new SkillGroup(playerNumber == 0 ? Palette.ROSE : Palette.OBSERVATORY))
                    .add(new Shim(1f, 50f))
                    .add(new TurnLabel(star))
                    .setConstraint(AxisLayout.stretched());

            add(textGroup,
                    new PointLabel());

            addStyles(Style.BACKGROUND.is(Background.solid(player.color).inset(context.game.bounds.percentOfHeight(0.01f))),
                    Style.HALIGN.left,
                    Style.VALIGN.top);

            watchForSkillChanges();
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

        final class PointLabel extends Label {
            private PointLabel() {
                super(player.storyPoints.get().toString());
                player.storyPoints.connect(new Slot<Integer>() {
                    @Override
                    public void onEmit(Integer integer) {
                        text.update(integer.toString());
                    }
                });
            }

            @Override
            protected Class<?> getStyleClass() {
                return PointLabel.class;
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

}
