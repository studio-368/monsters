/*
 * Copyright 2016 Traveler's Notebook: Monster Tales project authors
 *
 * This file is part of monsters
 *
 * monsters is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * monsters is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with monsters.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.bsu.storygame.core.view;

import com.google.common.collect.Lists;
import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.model.*;
import edu.bsu.storygame.core.util.IconScaler;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;
import pythagoras.f.Rectangle;
import react.*;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public final class NotebookLayer extends AbstractBook {

    private final IDimension closedSize;
    private final Stylesheet stylesheet;
    private final GameContext context;
    private final Player player;

    public final UnitSignal onDone = new UnitSignal();

    public NotebookLayer(final Player player, IDimension closedSize, final GameContext context) {
        super(context.game, ((ScreenStack.UIScreen) context.game.screenStack.top()).iface.anim,
                new Rectangle(0, 50, closedSize.width() * 2, closedSize.height()));

        this.closedSize = new Dimension(closedSize);
        this.stylesheet = GameStyle.newSheet(context.game);
        this.context = checkNotNull(context);
        this.player = checkNotNull(player);

        context.phase.connect(new Slot<Phase>() {
            @Override
            public void onEmit(Phase phase) {
                if (player == context.currentPlayer.get()) {
                    if (phase == Phase.STORY) {
                        turnPage();
                    } else if (phase == Phase.CONCLUSION) {
                        turnPage();
                    }
                }
            }
        });

        assembleBookFrom(new Layer[]{
                new CoverPage(),
                new EncounterPage(),
                new ReactionPage(),
                new StoryPage(),
                new SkillsPage(),
                new ConclusionPage(),
                new EndPage()
        });
    }

    private abstract class PageLayer extends GroupLayer {
        protected final int color;
        protected final Interface iface;
        protected final Root root;

        protected PageLayer(Layout layout) {
            super(closedSize.width(), closedSize.height());
            color = player.color;
            iface = ((ScreenStack.UIScreen) context.game.screenStack.top()).iface;
            root = iface.createRoot(layout, stylesheet, this)
                    .setSize(closedSize)
                    .addStyles(Style.BACKGROUND.is(Background.solid(color)));

            iface.removeRoot(root);

            onAdded(new Slot<Layer>() {
                @Override
                public void onEmit(Layer layer) {
                    iface.addRoot(root);
                    PageLayer.this.add(root.layer);
                }
            });
            onRemoved(new Slot<Layer>() {
                @Override
                public void onEmit(Layer layer) {
                    iface.removeRoot(root);
                }
            });
        }
    }


    private final class CoverPage extends PageLayer {
        private CoverPage() {
            super(AxisLayout.vertical().offStretch());
            root.add(new Label(player.name + "'s Story")
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

    private final class EncounterPage extends PageLayer {
        private EncounterPage() {
            super(AxisLayout.vertical());
            context.encounter.connect(new ValueView.Listener<Encounter>() {
                @Override
                public void onChange(Encounter encounter, Encounter t1) {
                    if (encounter == null) {
                        root.removeAll();
                    } else if (context.currentPlayer.get() == player) {
                        root.add(new Label("I encountered a ").addStyles(
                                Style.FONT.is(Typeface.HANDWRITING.in(context.game).atSize(0.045f))));
                        root.add(new EncounterImage(encounter));
                        root.add(new Label(encounter.name).addStyles(
                                Style.FONT.is(Typeface.HANDWRITING.in(context.game).atSize(0.045f))));
                    }
                }
            });
        }

        private class EncounterImage extends Label {
            private IconScaler scaler;
            final float IMAGE_SIZE = 0.8f;

            private EncounterImage(Encounter encounter) {
                this.scaler = new IconScaler(context.game);
                ImageCache.Key imageKey;
                try {
                    imageKey = ImageCache.Key.valueOf(encounter.imageKey.toUpperCase());
                } catch (IllegalArgumentException e) {
                    imageKey = ImageCache.Key.MISSING_IMAGE;
                }
                final float desiredWidth = IMAGE_SIZE * EncounterPage.this.width();
                Icon scaledIcon = scaler.scale(imageKey, desiredWidth);
                icon.update(scaledIcon);
            }
        }
    }

    private final class ReactionPage extends PageLayer {
        private ReactionPage() {
            super(AxisLayout.vertical());
            root.add(new Label("I decided to"));
            root.add(new ReactionGroup());
        }

        private final class ReactionGroup extends Group {

            public ReactionGroup() {
                super(AxisLayout.vertical());
                context.encounter.connect(new Slot<Encounter>() {
                    @Override
                    public void onEmit(Encounter encounter) {
                        removeAll();
                        if (encounter != null) {
                            add(makeReactionButtonAreaFor(encounter));
                        }
                    }
                });

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

    private final class StoryPage extends PageLayer {
        protected StoryPage() {
            super(AxisLayout.vertical());
            context.reaction.connect(new ValueView.Listener<Reaction>() {
                @Override
                public void onChange(Reaction reaction, Reaction t1) {
                    if (reaction == null) {
                        root.removeAll();
                    } else if (context.currentPlayer.get() == player) {
                        root.add(new Label(reaction.story.text).addStyles(Style.TEXT_WRAP.is(true)));
                    }
                }
            });
        }
    }

    private final class SkillsPage extends PageLayer {
        private List<TriggerButton> buttons = Lists.newArrayList();

        protected SkillsPage() {
            super(AxisLayout.vertical());
            context.reaction.connect(new Slot<Reaction>() {
                @Override
                public void onEmit(Reaction reaction) {
                    if (reaction == null) {
                        root.removeAll();
                        buttons.clear();
                    } else if (context.currentPlayer.get() == player) {
                        root.add(new Label("You used:"));
                        for (SkillTrigger skillTrigger : reaction.story.triggers) {
                            TriggerButton button = new TriggerButton(skillTrigger.skill.name, skillTrigger.conclusion);
                            button.setEnabled(context.currentPlayer.get().skills.contains(skillTrigger.skill));
                            root.add(button);
                            buttons.add(button);
                        }
                        TriggerButton noSkill = new TriggerButton("No Skill",
                                context.reaction.get().story.noSkill.conclusion);
                        root.add(noSkill);
                        buttons.add(noSkill);
                    }
                }
            });
            context.phase.connect(new Slot<Phase>() {
                @Override
                public void onEmit(Phase phase) {
                    if (phase == Phase.CONCLUSION) {
                        for (TriggerButton button : buttons) {
                            button.setEnabled(false);
                        }
                    }
                }
            });
        }

        private final class TriggerButton extends Button {

            public TriggerButton(final String name, final Conclusion conclusion) {
                super(name);
                onClick(new Slot<Button>() {
                    @Override
                    public void onEmit(Button button) {
                        context.conclusion.update(conclusion);
                        context.phase.update(Phase.CONCLUSION);
                    }
                });
            }
        }
    }

    private final class ConclusionPage extends PageLayer {

        protected ConclusionPage() {
            super(AxisLayout.vertical());
            context.conclusion.connect(new SignalView.Listener<Conclusion>() {
                @Override
                public void onEmit(Conclusion conclusion) {
                    if (conclusion == null) {
                        root.removeAll();
                    } else {
                        root.add(new Label(conclusion.text).addStyles(Style.TEXT_WRAP.on));
                        root.add(new EncounterRewardLabel(conclusion));
                    }
                }
            });
        }

        final class EncounterRewardLabel extends Label {
            private EncounterRewardLabel(Conclusion conclusion) {
                super();
                addStyles(Style.TEXT_WRAP.on);
                StringBuilder stringBuilder = new StringBuilder();
                if (conclusion.points > 0) {
                    stringBuilder.append("You gain ")
                            .append(String.valueOf(conclusion.points))
                            .append(" story points");
                }
                if (conclusion.skill != null) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(" and the ")
                                .append(conclusion.skill.name)
                                .append(" skill");
                    } else {
                        stringBuilder.append("You gain the ")
                                .append(conclusion.skill.name)
                                .append(" skill");
                    }
                }
                text.update(stringBuilder.toString());
            }

        }
    }

    private final class EndPage extends PageLayer {
        protected EndPage() {
            super(AxisLayout.vertical());
            final Button button = new Button("Close notebook").onClick(new Slot<Button>() {
                @Override
                public void onEmit(Button button) {
                    applyModelChanges(context.conclusion.get());
                    onDone.emit();
                    button.setEnabled(false);
                }
            });
            root.add(button);
            context.phase.connect(new Slot<Phase>() {
                @Override
                public void onEmit(Phase phase) {
                    if (phase == Phase.CONCLUSION) {
                        button.setEnabled(true);
                    }
                }
            });
        }

        private void applyModelChanges(Conclusion conclusion) {
            Player currentPlayer = context.currentPlayer.get();
            currentPlayer.storyPoints.update(
                    currentPlayer.storyPoints.get() + conclusion.points);
            if (isThereASkillRewardThisPlayerDoesNotHave(conclusion)) {
                context.currentPlayer.get().skills.add(conclusion.skill);
            }
        }


        private boolean isThereASkillRewardThisPlayerDoesNotHave(Conclusion conclusion) {
            Player currentPlayer = context.currentPlayer.get();
            return conclusion.skill != null && !currentPlayer.skills.contains(conclusion.skill);
        }
    }
}
