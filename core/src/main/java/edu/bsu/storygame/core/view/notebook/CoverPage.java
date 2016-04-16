/*
 * Copyright 2016 Traveler's Notebook: Monster Tales project authors
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

package edu.bsu.storygame.core.view.notebook;

import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import edu.bsu.storygame.core.model.Skill;
import edu.bsu.storygame.core.view.GameStyle;
import playn.scene.GroupLayer;
import react.RList;
import react.Slot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class CoverPage extends GroupLayer {

    private final Player player;

    public CoverPage(Interface iface, GameContext context, Player player) {
        this.player = checkNotNull(player);
        iface.createRoot(AxisLayout.vertical(), GameStyle.newSheet(context.game), this)
                .setSize(400, 300)
                .add(new Label(player.name + "'s Story")
                                .addStyles(Style.HALIGN.left),
                        new ScoreLabel()
                                .addStyles(Style.HALIGN.left),
                        new SkillGroup().addStyles(Style.HALIGN.left),
                        new Shim(0, 0).setConstraint(AxisLayout.stretched()));

    }

    private final class ScoreLabel extends Label {
        private ScoreLabel() {
            super("Story Points: 0");
            player.storyPoints.connect(new Slot<Integer>() {
                @Override
                public void onEmit(Integer integer) {
                    text.update("Score: " + integer);
                }
            });
        }
    }

    private final class SkillGroup extends Group {

        private SkillGroup() {
            super(AxisLayout.horizontal().offStretch());
            updatePlayerSkills();
            player.skills.connect(new RList.Listener<Skill>() {
                @Override
                public void onAdd(Skill skill) {
                    SkillGroup.this.updatePlayerSkills();
                }

                @Override
                public void onRemove(Skill skill) {
                    SkillGroup.this.updatePlayerSkills();
                }
            });
        }

        private void updatePlayerSkills() {
            this.removeAll();
            int skillCounter = 1;
            this.add(new Label("Skills: ")
                    .addStyles(Style.HALIGN.left));
            for (Skill skill : player.skills) {
                if (!(player.skills.size() == skillCounter)) {
                    this.add(new Label(skill.name), new Label(", "));
                } else {
                    this.add(new Label(skill.name));
                }
                skillCounter++;
            }
        }
    }
}
