package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.model.*;
import edu.bsu.storygame.core.util.IconScaler;
import playn.core.Font;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;
import react.RList;
import react.Slot;
import react.UnitSignal;
import tripleplay.anim.Animator;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.FlowLayout;
import tripleplay.util.Colors;

import static com.google.common.base.Preconditions.checkNotNull;

public final class NotebookLayer extends GroupLayer {

    public static final float OPEN_CLOSE_ANIM_DURATION = 400f;

    private final IDimension closedSize;
    private final Stylesheet stylesheet;
    private final GameContext context;
    private final Layer cover;
    private final Layer encounterPage;
    private final Layer reactionPage;
    private final Player player;

    public final UnitSignal onDone = new UnitSignal();

    public NotebookLayer(Player player, IDimension closedSize, GameContext context) {
        super(closedSize.width() * 2, closedSize.height());
        this.closedSize = new Dimension(closedSize);
        this.stylesheet = GameStyle.newSheet(context.game);
        this.context = checkNotNull(context);
        this.player = checkNotNull(player);

        setHandleToTopCenter();

        this.cover = new CoverPage();
        this.encounterPage = new EncounterPage();
        this.reactionPage = new ReactionPage();

        addPages(cover, encounterPage, reactionPage);
    }

    private void addPages(Layer... layers) {
        for (int i = layers.length - 1; i >= 0; i--) {
            addAt(layers[i], closedSize.width(), 0);
            layers[i].setDepth(layers.length - i);
        }
    }

    private abstract class PageLayer extends GroupLayer {

        protected final int color;
        protected final Interface iface;

        protected PageLayer() {
            super(closedSize.width(), closedSize.height());
            color = (player == context.players.get(0)) ? Palette.PLAYER_ONE : Palette.PLAYER_TWO;
            iface = ((ScreenStack.UIScreen) context.game.screenStack.top()).iface;
        }
    }


    private final class CoverPage extends PageLayer {

        private CoverPage() {
            Root root = iface.createRoot(AxisLayout.vertical().offStretch(), stylesheet, this)
                    .setSize(closedSize)
                    .addStyles(Style.BACKGROUND.is(Background.solid(color)));
            root.add(new Label(player.name + "'s Story")
                            .addStyles(Style.HALIGN.left),
                    new ScoreLabel()
                            .addStyles(Style.HALIGN.left),
                    new SkillGroup().addStyles(Style.HALIGN.left),
                    new Shim(0, 0).setConstraint(AxisLayout.stretched()));
        }
    }

    private final class EncounterPage extends PageLayer {

        private EncounterPage() {
            iface.createRoot(AxisLayout.vertical(), stylesheet, this)
                    .setSize(closedSize)
                    .addStyles(Style.BACKGROUND.is(Background.solid(color)))
                    .add(new EncounterGroup());
        }
    }

    private class EncounterGroup extends Group {

        public EncounterGroup() {
            super(AxisLayout.vertical());
            context.encounter.connect(new Slot<Encounter>() {
                @Override
                public void onEmit(Encounter encounter) {
                    removeAll();
                    if (encounter != null) {
                        Label encounterLabel = new Label("I encountered a ");
                        add(encounterLabel);
                        EncounterImage encounterImage = new EncounterImage(encounter);
                        EncounterNameLabel encounterName = new EncounterNameLabel(encounter);
                        add(encounterImage);
                        add(encounterName);

                    }
                }
            });
            context.reaction.connect(new Slot<Reaction>() {
                @Override
                public void onEmit(Reaction reaction) {
                    removeAll();
                    if (reaction != null) {
                        add(new EncounterReactionStory(reaction.story).addStyles(Style.TEXT_WRAP.is(true)));
                    }
                }
            });
            context.conclusion.connect(new Slot<Conclusion>() {
                @Override
                public void onEmit(Conclusion conclusion) {
                    removeAll();
                    if (conclusion != null) {
                        add(new EncounterRewardLabel(conclusion));
                    }
                }
            });
        }
    }

    private class EncounterImage extends Label {

        private IconScaler scaler;

        private EncounterImage(Encounter encounter) {

            final float IMAGE_SIZE = 0.8f;

            this.scaler = new IconScaler(context.game);

            ImageCache.Key imageKey;
            try {
                imageKey = ImageCache.Key.valueOf(encounter.imageKey.toUpperCase());
            } catch (IllegalArgumentException e) {
                imageKey = ImageCache.Key.MISSING_IMAGE;
            }
             final float desiredWidth = IMAGE_SIZE * encounterPage.width();
            Icon scaledIcon = scaler.scale(imageKey, desiredWidth);
            icon.update(scaledIcon);


        }
    }

    private class EncounterNameLabel extends Label {
        private EncounterNameLabel(Encounter encounter){
            text.update(encounter.name);

        }
    }

    private class EncounterReactionStory extends Label {

        EncounterReactionStory(Story story) {
            super(story.text);
        }

    }

    final class EncounterRewardLabel extends Label {
        private EncounterRewardLabel(Conclusion conclusion) {
            super();
            addStyles(Style.TEXT_WRAP.on);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(context.conclusion.get().text);
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

    private final class ReactionPage extends PageLayer {
        private ReactionPage() {
            iface.createRoot(AxisLayout.vertical(), stylesheet, this)
                    .setSize(closedSize)
                    .addStyles(Style.BACKGROUND.is(Background.solid(color)))
                    .add(new ReactionGroup());

        }
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
            context.phase.connect(new Slot<Phase>() {
                @Override
                public void onEmit(Phase phase) {
                    if (phase.equals(Phase.STORY)) {
                        removeAll();
                        add(makeSkillTriggersFor(context.reaction.get().story));
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

        private Group makeSkillTriggersFor(Story story) {
            Group buttonGroup = new Group(new FlowLayout());
            for (final SkillTrigger trigger : story.triggers) {
                if (context.currentPlayer.get().skills.contains(trigger.skill)) {
                    Button skillButton = new TriggerButton(trigger.skill.name, trigger.conclusion);
                    buttonGroup.add(skillButton);
                }
            }
            buttonGroup.add(new TriggerButton("No skill", story.noSkill.conclusion));
            return buttonGroup;
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

        final class TriggerButton extends Button {
            private TriggerButton(final String name, final Conclusion conclusion) {
                super(name);
                onClick(new Slot<Button>() {
                    private final Player currentPlayer = context.currentPlayer.get();

                    @Override
                    public void onEmit(Button button) {
                        ReactionGroup.this.removeAll();
                        context.conclusion.update(conclusion);
                        applyModelChanges(conclusion);
                        add(new DoneButton());
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

        final class DoneButton extends Button {
            private DoneButton() {
                super("Done");
                onClick(new Slot<Button>() {
                    @Override
                    public void onEmit(Button button) {
                        onDone.emit();
                    }
                });
            }
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
                    this.add(new SkillLabel(skill), new Label(", "));
                } else {
                    this.add(new SkillLabel(skill));
                }
                skillCounter++;
            }
        }
    }

    private final class SkillLabel extends Label {

        private SkillLabel(Skill skill) {
            super(skill.name);
            addStyles(Style.HALIGN.left);
        }
    }


    /**
     * Set the origin to the top center.
     * <p/>
     * The origin acts as a handle for agents outside of this layer. When they
     * set this layer's translation, it's with respect to this origin value. By
     * setting this to the top-center, we are always thinking of holding the book
     * from the spine, whether it is open or closed.
     */
    private void setHandleToTopCenter() {
        setOrigin(Origin.TC);
    }

    /**
     * Open the book.
     * <p/>
     * There is not currently a real "flip" animation, and so this does a page shuffle animation instead,
     * like a stack of loose pages.
     *
     * @param anim the animator
     */
    public void open(Animator anim) {
        anim.tweenX(cover)
                .to(0)
                .in(OPEN_CLOSE_ANIM_DURATION)
                .easeIn()
                .then()
                .action(new LayerOnTop(encounterPage))
                .then()
                .tweenX(encounterPage)
                .to(0)
                .in(OPEN_CLOSE_ANIM_DURATION)
                .easeIn();
    }

    public void close(Animator anim) {
        final float centerX = width() / 2f;
        anim.tweenX(encounterPage)
                .to(centerX)
                .in(OPEN_CLOSE_ANIM_DURATION)
                .easeIn()
                .then()
                .action(new LayerOnTop(cover))
                .then()
                .tweenX(cover)
                .to(centerX)
                .in(OPEN_CLOSE_ANIM_DURATION)
                .easeIn();
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

    /**
     * Raises a layer's z-depth above those of its neighboring pages, so that it renders on top of them.
     */
    private static final class LayerOnTop implements Runnable {

        private final Layer layer;

        private LayerOnTop(Layer layer) {
            this.layer = checkNotNull(layer);
        }

        @Override
        public void run() {
            layer.setDepth(layer.depth() + 2);
        }
    }
}