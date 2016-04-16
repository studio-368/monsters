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

import edu.bsu.storygame.core.model.*;
import edu.bsu.storygame.core.view.GameStyle;
import react.Connection;
import react.Slot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;

public class ReactionPage extends PageLayer {

    private Connection connection;

    public ReactionPage(Interface iface, GameContext context, Player player) {
        super(iface, context, player);
    }

    @Override
    protected Root createRoot() {
        root = iface.createRoot(AxisLayout.vertical(), GameStyle.newSheet(context.game), this)
                .setSize(400, 300)
                .addStyles(Style.BACKGROUND.is(Background.solid(Colors.RED)));
        connection = context.encounter.connect(new Slot<Encounter>() {
            @Override
            public void onEmit(Encounter encounter) {
                if (encounter != null) {
                    root.add(new Label("I decided to"),
                            new ReactionGroup(encounter));
                } else {
                    removeAll();
                }
            }
        });
        return root;
    }

    @Override
    protected void wasRemoved() {
        connection.close();
        connection = null;
    }

    private final class ReactionGroup extends Group {

        public ReactionGroup(Encounter encounter) {
            super(AxisLayout.vertical());
            add(makeReactionButtonAreaFor(encounter));
        }

        private Group makeReactionButtonAreaFor(Encounter encounter) {
            Group group = new Group(new AxisLayout.Vertical());
            for (Reaction reaction : encounter.reactions) {
                group.add(new ReactionButton(reaction));
            }
            return group;
        }

        final class ReactionButton extends Button {
            private ReactionButton(final Reaction reaction) {
                super(reaction.name);
                onClick(new Slot<Button>() {
                    @Override
                    public void onEmit(Button button) {
                        context.reaction.update(reaction);
                        context.phase.update(Phase.HANDOFF);
                    }
                });
                context.phase.connect(new Slot<Phase>() {
                    @Override
                    public void onEmit(Phase phase) {
                        setEnabled(phase.equals(Phase.ENCOUNTER));
                    }
                });
            }
        }
    }

}
