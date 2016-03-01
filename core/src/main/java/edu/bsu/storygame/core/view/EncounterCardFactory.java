package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.model.*;
import playn.core.Image;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import react.Slot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AbsoluteLayout;
import tripleplay.ui.layout.AxisLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class EncounterCardFactory {

    private final GameContext context;


    public EncounterCardFactory(GameContext context) {
        this.context = checkNotNull(context);
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
                            final Image image = context.game.imageCache.image(imageKey);
                            final Icon unscaledIcon = Icons.image(image);
                            final float scale = IMAGE_SIZE * size().width() / image.width();
                            final Icon scaledIcon = Icons.scaled(unscaledIcon, scale);
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
                            for (Reaction reaction : encounter.reactions) {
                                add(new ReactionButton(reaction));
                            }
                        }
                    }
                });
                context.phase.connect(new Slot<Phase>() {
                    @Override
                    public void onEmit(Phase phase) {
                        if (phase.equals(Phase.STORY)) {
                            removeAll();
                            final Story story = reaction.story;
                            add(new StoryLabel(story));
                            for (final SkillTrigger trigger : story.triggers) {
                                Button skillButton = new SkillTriggerButton(trigger);
                                add(skillButton);
                            }
                        }
                    }
                });
            }

            final class SkillTriggerButton extends Button {
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
                                    new Button("Done").onClick(new Slot<Button>() {
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

                @Override
                protected Class<?> getStyleClass() {
                    return SkillTriggerButton.class;
                }
            }

            final class StoryLabel extends Label {
                private StoryLabel(Story story) {
                    super(story.text);
                }

                @Override
                protected Class<?> getStyleClass() {
                    return StoryLabel.class;
                }
            }

            final class ReactionButton extends Button {

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

            final class ConclusionLabel extends Label {
                private ConclusionLabel(Conclusion conclusion) {
                    super(conclusion.text);
                }

                @Override
                protected Class<?> getStyleClass() {
                    return ConclusionLabel.class;
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
