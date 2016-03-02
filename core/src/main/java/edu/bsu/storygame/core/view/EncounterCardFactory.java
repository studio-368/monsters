package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.model.*;
import edu.bsu.storygame.core.util.IconScaler;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import react.Slot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AbsoluteLayout;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.FlowLayout;

import static com.google.common.base.Preconditions.*;

public class EncounterCardFactory {

    private final GameContext context;
    private final IconScaler scaler;

    public EncounterCardFactory(GameContext context) {
        this.context = checkNotNull(context);
        scaler = new IconScaler(context.game);
    }

    public Layer create(float width, float height, Interface iface) {
        final GroupLayer layer = new GroupLayer(width, height);
        iface.createRoot(new AbsoluteLayout(), GameStyle.newSheet(context.game), layer)
                .setSize(width, height)
                .add(AbsoluteLayout.at(new EncounterCard(), 0, 0, width, height));
        return layer;
    }

    final class EncounterCard extends Group {
        private final TitleLabel titleLabel = new TitleLabel();
        private final InteractionArea area = new InteractionArea();

        private EncounterCard() {
            super(AxisLayout.vertical().offStretch());
            add(titleLabel);
            add(area);
            add(new Shim(0, 0).setConstraint(AxisLayout.stretched()));
        }

        @Override
        protected Class<?> getStyleClass() {
            return EncounterCard.class;
        }

        final class TitleLabel extends Label {
            private TitleLabel() {
                context.encounter.connect(new Slot<Encounter>() {
                    public static final float IMAGE_SIZE = 0.80f;

                    @Override
                    public void onEmit(Encounter encounter) {
                        if (encounter != null) {
                            text.update(encounter.name);
                            final ImageCache.Key imageKey = ImageCache.Key.valueOf(encounter.imageKey.toUpperCase());
                            final float desiredWidth = IMAGE_SIZE * size().width();
                            Icon scaledIcon = scaler.scale(imageKey, desiredWidth);
                            icon.update(scaledIcon);
                        }
                    }
                });
            }

            @Override
            protected Class<?> getStyleClass() {
                return TitleLabel.class;
            }
        }

        class InteractionArea extends Group {

            private Reaction reaction;

            private InteractionArea() {
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
                context.phase.connect(new Slot<Phase>() {
                    @Override
                    public void onEmit(Phase phase) {
                        if (phase.equals(Phase.STORY)) {
                            removeAll();
                            add(makeStoryAndSkillsAreaFor(reaction.story));
                        }
                    }
                });
            }

            private Group makeReactionButtonAreaFor(Encounter encounter) {
                Group group = new Group(new FlowLayout());
                for (Reaction reaction : encounter.reactions) {
                    group.add(new ReactionButton(reaction));
                }
                return group;
            }

            private Group makeStoryAndSkillsAreaFor(Story story) {
                Group group = new Group(AxisLayout.vertical().offStretch());
                group.add(new StoryLabel(story));
                Group buttonGroup = new Group(new FlowLayout());
                for (final SkillTrigger trigger : story.triggers) {
                    Button skillButton = new SkillTriggerButton(trigger);
                    buttonGroup.add(skillButton);
                }
                group.add(buttonGroup);
                return group;
            }

            protected class StyledButton extends Button {
                protected StyledButton(String text) {
                    super(text);
                }

                @Override
                protected Class<?> getStyleClass() {
                    return StyledButton.class;
                }
            }


            final class SkillTriggerButton extends StyledButton {
                private SkillTriggerButton(final SkillTrigger trigger) {
                    super(trigger.skill.name);
                    onClick(new Slot<Button>() {
                        private final Player currentPlayer = context.currentPlayer.get();

                        @Override
                        public void onEmit(Button button) {
                            final Conclusion conclusion = trigger.conclusion;
                            InteractionArea.this.removeAll();
                            InteractionArea.this.add(new ConclusionLabel(conclusion),
                                    new RewardLabel(trigger.conclusion),
                                    new StyledButton("Done").onClick(new Slot<Button>() {
                                        {
                                            context.phase.connect(new Slot<Phase>() {
                                                @Override
                                                public void onEmit(Phase phase) {
                                                    setEnabled(phase.equals(Phase.STORY));
                                                }
                                            });
                                        }

                                        @Override
                                        public void onEmit(Button button) {
                                            context.phase.update(Phase.END_OF_ROUND);
                                        }
                                    }));
                            applyModelChanges(conclusion);
                        }

                        private void applyModelChanges(Conclusion conclusion) {
                            currentPlayer.storyPoints.update(
                                    currentPlayer.storyPoints.get() + conclusion.points);
                            if (isThereASkillRewardThisPlayerDoesNotHave(conclusion)) {
                                context.currentPlayer.get().skills.add(conclusion.skill);
                            }
                        }

                        private boolean isThereASkillRewardThisPlayerDoesNotHave(Conclusion conclusion) {
                            return conclusion.skill != null && !currentPlayer.skills.contains(conclusion.skill);
                        }
                    });
                }
            }

            protected abstract class StyledNarrativeLabel extends Label {
                protected StyledNarrativeLabel(String text) {
                    super(text);
                }

                @Override
                protected Class<?> getStyleClass() {
                    return StyledNarrativeLabel.class;
                }
            }

            final class StoryLabel extends StyledNarrativeLabel {
                private StoryLabel(Story story) {
                    super(story.text);
                }
            }

            final class ReactionButton extends StyledButton {
                private ReactionButton(final Reaction reaction) {
                    super(reaction.name);
                    onClick(new Slot<Button>() {
                        @Override
                        public void onEmit(Button button) {
                            InteractionArea.this.reaction = reaction;
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

            final class ConclusionLabel extends StyledNarrativeLabel {
                private ConclusionLabel(Conclusion conclusion) {
                    super(conclusion.text);
                }
            }

            final class RewardLabel extends Label {
                private RewardLabel(Conclusion conclusion) {
                    super();
                    StringBuilder stringBuilder = new StringBuilder();
                    if (conclusion.points > 0) {
                        stringBuilder.append("You gain ")
                                .append(String.valueOf(conclusion.points))
                                .append(" story points");
                    }
                    if (conclusion.skill != null) {
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append(" and the ")
                                    .append(conclusion.skill)
                                    .append(" skill");
                        } else {
                            stringBuilder.append("You gain the ")
                                    .append(conclusion.skill.name)
                                    .append(" skill");
                        }
                    }
                    text.update(stringBuilder.toString());
                }

                @Override
                protected Class<?> getStyleClass() {
                    return RewardLabel.class;
                }
            }
        }


    }


}
