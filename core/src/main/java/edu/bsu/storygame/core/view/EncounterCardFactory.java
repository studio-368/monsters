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
import tripleplay.ui.layout.FlowLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class EncounterCardFactory {

    private final GameContext context;


    public EncounterCardFactory(GameContext context) {
        this.context = checkNotNull(context);
    }

    public Layer create(float width, float height, Interface iface) {
        final GroupLayer layer = new GroupLayer(width, height);
        layer.setVisible(false);
        context.phase.connect(new Slot<Phase>() {
            @Override
            public void onEmit(Phase phase) {
                if (phase.equals(Phase.ENCOUNTER)) {
                    layer.setVisible(true);
                } else if (phase.equals(Phase.END_OF_ROUND)) {
                    layer.setVisible(false);
                }
            }
        });

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
                super(new FlowLayout());
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
                                Button skillButton = new Button(trigger.skill);
                                skillButton.onClick(new Slot<Button>() {
                                    @Override
                                    public void onEmit(Button button) {
                                        InteractionArea.this.removeAll();
                                        InteractionArea.this.add(new ConclusionLabel(trigger.conclusion),
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
                                    }
                                });
                                add(skillButton);
                            }
                        }
                    }
                });
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
                private ConclusionLabel(String text) {
                    super(text);
                }

                @Override
                protected Class<?> getStyleClass() {
                    return ConclusionLabel.class;
                }
            }
        }


    }


}
