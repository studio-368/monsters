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

package edu.bsu.storygame.core.view;

import com.google.common.collect.Lists;
import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.AudioCache;
import edu.bsu.storygame.core.assets.AudioRandomizer;
import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.model.*;
import edu.bsu.storygame.core.util.IconScaler;
import edu.bsu.storygame.core.util.MixedCase;
import edu.bsu.storygame.core.util.Shuffler;
import playn.core.*;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;
import react.*;
import tripleplay.anim.AnimGroup;
import tripleplay.anim.Animation;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.TableLayout;
import tripleplay.util.Colors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public final class NotebookLayer extends GroupLayer {

    public static final float OPEN_CLOSE_ANIM_DURATION = 400f;
    private static final float TOP = 100;
    private static final String BULLET_AND_SPACE = "\u2022 ";

    private final IDimension closedSize;
    private final Stylesheet stylesheet;
    private final GameContext context;
    private final PageLayer cover;
    private final PageLayer encounterPage;
    private final PageLayer reactionPage;
    private final PageLayer storyPage;
    private final PageLayer skillsPage;
    private final PageLayer conclusionPage;
    private final PageLayer endPage;
    private final Player player;
    private final Interface iface;
    private final PageLayer[] pages;
    private float depthCounter = 0;

    private final AudioRandomizer audioRandomizer = new AudioRandomizer();
    private final NotebookImageLoader notebookImageLoader;

    public final UnitSignal onDone = new UnitSignal();

    public NotebookLayer(final Player player, IDimension closedSize, final GameContext context) {
        super(closedSize.width() * 2, closedSize.height());
        this.notebookImageLoader = new NotebookImageLoader();
        this.iface = ((ScreenStack.UIScreen) context.game.screenStack.top()).iface;

        this.closedSize = new Dimension(closedSize);
        this.stylesheet = GameStyle.newSheet(context.game);
        this.context = checkNotNull(context);
        this.player = checkNotNull(player);

        setOrigin(Origin.TC);

        this.cover = new CoverPage();
        this.encounterPage = new EncounterPage();
        this.reactionPage = new ReactionPage();
        this.storyPage = new StoryPage();
        this.skillsPage = new SkillsPage();
        this.conclusionPage = new ConclusionPage();
        this.endPage = new EndPage();

        pages = new PageLayer[]{
                cover,
                encounterPage,
                reactionPage,
                storyPage,
                skillsPage,
                conclusionPage,
                endPage
        };
        addPages(pages);

        context.phase.connect(new Slot<Phase>() {
            @Override
            public void onEmit(Phase phase) {
                if (player == context.currentPlayer.get()) {
                    if (phase == Phase.STORY) {
                        turnToStory();
                    } else if (phase == Phase.CONCLUSION) {
                        turnToConclusion();
                    }
                }
            }
        });

    }

    private void addPages(Layer... layers) {
        for (int i = layers.length - 1; i >= 0; i--) {
            addAt(layers[i], closedSize.width(), 0);
            layers[i].setDepth(layers.length - i);
        }
    }

    private abstract class PageLayer extends GroupLayer {
        protected final Interface iface;
        protected final Root root;

        protected PageLayer(Layout layout) {
            super(closedSize.width(), closedSize.height());
            iface = ((ScreenStack.UIScreen) context.game.screenStack.top()).iface;
            root = iface.createRoot(layout, stylesheet, this)
                    .setSize(closedSize)
                    .addStyles(Style.BACKGROUND.is(Background.image(notebookImageLoader.next())));
        }
    }


    private final class CoverPage extends PageLayer {
        protected final int color;

        ProgressBar progressBar;
        Layer pointsLayer;

        private CoverPage() {
            super(AxisLayout.vertical().offStretch());
            color = player.color;
            Image coverPage = createTintedCoverPage();
            root.addStyles(Style.BACKGROUND.is(Background.image(coverPage)));
            configureProgressBar();
            root.add(new Label(player.name + "'s Story")
                            .addStyles(Style.HALIGN.center),
                    new SkillGroup().addStyles(Style.HALIGN.center),
                    new Shim(0, 0).setConstraint(AxisLayout.stretched()));
            addAt(progressBar, this.width() * 1 / 25, this.height() * 1 / 25);
            add(pointsLayer = new ImageLayer());
            setPointLayer(context.game, 0xFF000000, 0);
        }

        private void setPointLayer(MonsterGame game, int color, int points) {
            remove(pointsLayer);
            Graphics gfx = context.game.plat.graphics();
            Font font = Typeface.GAME_TEXT.font;
            TextFormat format = new TextFormat(font);
            TextLayout textLayout = gfx.layoutText("Points: \n" + points, format);
            Canvas canvas = game.plat.graphics().createCanvas(textLayout.size);
            canvas.setFillColor(color).fillText(textLayout, 0, 0);
            pointsLayer = new ImageLayer(canvas.toTexture());
            addAt(pointsLayer, progressBar.width() * 2 / 5, progressBar.height() / 2);
        }

        private Image createTintedCoverPage() {
            Image cover;
            if (color == Palette.PLAYER_ONE) {
                cover = context.game.imageCache.image(ImageCache.Key.COVER_1);
            } else {
                cover = context.game.imageCache.image(ImageCache.Key.COVER_2);
            }
            return cover;
        }

        private final class SkillColumn extends TableLayout.Column {
            protected SkillColumn(Style.HAlign hAlign, boolean stretch, float weight, float minWidth) {
                super(hAlign, stretch, weight, minWidth);
            }
        }

        private final class SkillGroup extends Group {

            private SkillGroup() {
                super(new TableLayout(new SkillColumn(Style.HAlign.CENTER, true, .03f, .1f),
                        new SkillColumn(Style.HAlign.LEFT, true, .01f, .1f)));
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
                for (Skill skill : player.skills) {
                    this.add(new Label(BULLET_AND_SPACE + skill.name));
                }
            }
        }

        private void configureProgressBar() {
            final int max = context.pointsRequiredForVictory;
            final float width = this.width();
            final float height = this.height();
            progressBar = new ProgressBar(max, width * 0.15f, height * 0.18f, context.game, ProgressBar.FillType.VERTICAL);
            player.storyPoints.connect(new Slot<Integer>() {
                @Override
                public void onEmit(Integer integer) {
                    progressBar.increment(context.conclusion.get().points);
                    setPointLayer(context.game, 0xFF000000, integer);
                }
            });
        }
    }

    private final class EncounterPage extends PageLayer {
        private EncounterPage() {
            super(AxisLayout.vertical());
            context.encounter.connect(new ValueView.Listener<Encounter>() {
                @Override
                public void onChange(Encounter encounter, Encounter t1) {
                    if(encounter == null){
                        root.removeAll();
                    } else if (context.currentPlayer.get() == player){
                        root.add(createReminderPostedNote());
                        root.add(new Label("I encountered a ").addStyles(
                                Style.FONT.is(Typeface.HANDWRITING.in(context.game).atSize(0.045f)),
                                Style.COLOR.is(Colors.BLACK)));
                        root.add(new EncounterImage(encounter));
                        root.add(new Label(encounter.name).addStyles(
                                Style.FONT.is(Typeface.HANDWRITING.in(context.game).atSize(0.045f)),
                                Style.COLOR.is(Colors.BLACK)));
                    }
                }
            });
        }

        private Root createReminderPostedNote(){
            Image image = context.game.imageCache.image(ImageCache.Key.POSTED_NOTE);
            Root root = iface.createRoot(AxisLayout.vertical(), stylesheet, new GroupLayer(image.width(), image.height()))
                    .addStyles(Style.BACKGROUND.is(Background.image(image).inset(20,10)))
                    .add(new Label("Reminder").addStyles(
                    Style.COLOR.is(Colors.BLACK),Style.UNDERLINE.is(true),
                    Style.FONT.is(Typeface.HANDWRITING.in(context.game).atSize(0.03f))));
                    root = addPlayersSkillsToNote(root);
                    root.add(new Label("Current points: "+ player.storyPoints.get()).addStyles(Style.TEXT_WRAP.on,
                            Style.COLOR.is(Colors.BLACK),
                            Style.FONT.is(Typeface.HANDWRITING.in(context.game).atSize(0.027f))));
            return root;
        }

        private Root addPlayersSkillsToNote(Root root){
            root.add(new Label("Skills:").addStyles(
                    Style.COLOR.is(Colors.BLACK),
                    Style.FONT.is(Typeface.HANDWRITING.in(context.game).atSize(0.027f))));
            for(int i = 0; i < player.skills.size(); i++){
                if (player.skills.size() - (i) > 1){
                    root.add(new Label(BULLET_AND_SPACE + player.skills.get(i).name + "  " + BULLET_AND_SPACE + player.skills.get(i + 1).name).addStyles(Style.TEXT_WRAP.on,
                            Style.COLOR.is(Colors.BLACK),
                            Style.FONT.is(Typeface.HANDWRITING.in(context.game).atSize(0.02f))));
                            i++;
                        }
                else{
                    root.add(new Label(BULLET_AND_SPACE + player.skills.get(i).name).addStyles(Style.TEXT_WRAP.on,
                            Style.COLOR.is(Colors.BLACK),
                            Style.FONT.is(Typeface.HANDWRITING.in(context.game).atSize(0.02f))));
                    }
                }
            return root;
        }

        private class EncounterImage extends Label {
            private IconScaler scaler;
            final float IMAGE_SIZE = 0.7f;

            private EncounterImage(Encounter encounter) {
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
    }

    private final class ReactionPage extends PageLayer {
        private ReactionPage() {
            super(AxisLayout.vertical());
            root.add(new Label("I decided to").addStyles(Style.COLOR.is(Colors.BLACK)));
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
                    super(MixedCase.convert(reaction.name));
                    onClick(new Slot<Button>() {
                        @Override
                        public void onEmit(Button button) {
                            context.reaction.update(reaction);
                            context.story.update(reaction.stories.chooseOne());
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
                        context.story.update(null);
                        root.removeAll();
                    } else if (context.currentPlayer.get() == player) {
                        context.story.update(reaction.stories.chooseOne());
                        root.add(new Label(context.story.get().text).addStyles(
                                Style.TEXT_WRAP.is(true),
                                Style.COLOR.is(Colors.BLACK)));
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
                        root.add(createInstructionPostedNote("Ask " + context.currentPlayer.get().name + " which skill was used."));
                        for (SkillTrigger skillTrigger : context.story.get().triggers) {
                            TriggerButton button = new TriggerButton(skillTrigger.skill.name, skillTrigger.conclusion);
                            button.setEnabled(context.currentPlayer.get().skills.contains(skillTrigger.skill));
                            root.add(button);
                            buttons.add(button);
                        }
                        TriggerButton noSkill = new TriggerButton("No Skill",
                                context.story.get().noSkill.conclusion);
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

        private Root createInstructionPostedNote(String text){
            Image image = context.game.imageCache.image(ImageCache.Key.POSTED_NOTE);
            return iface.createRoot(AxisLayout.vertical(), stylesheet,  new GroupLayer())
                    .addStyles(Style.BACKGROUND.is(Background.image(image).inset(10,35)))
                    .add(new Label(text).addStyles(Style.TEXT_WRAP.on,
                            Style.COLOR.is(Colors.BLACK),
                            Style.FONT.is(Typeface.HANDWRITING.in(context.game).atSize(0.035f))));
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
                        root.add(new Label(conclusion.text).addStyles(
                                Style.TEXT_WRAP.on,
                                Style.COLOR.is(Colors.BLACK)));
                        root.add(new EncounterRewardLabel(conclusion));
                    }
                }
            });
        }

        final class EncounterRewardLabel extends Label {
            private EncounterRewardLabel(Conclusion conclusion) {
                super();
                addStyles(Style.TEXT_WRAP.on,
                        Style.COLOR.is(Colors.BLACK));
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
            final Button button = new Button("Close Notebook").onClick(new Slot<Button>() {
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

    /**
     * Open the book.
     * <p/>
     * There is not currently a real "flip" animation, and so this does a page shuffle animation instead,
     * like a stack of loose pages.
     */
    public void open() {
        depthCounter = 0;
        iface.anim.add(movePageLeft(cover))
                .then()
                .add(movePageLeft(encounterPage));
    }

    private Animation movePageLeft(final PageLayer layer) {
        context.game.audioCache.playSound(audioRandomizer.getKey(AudioRandomizer.Event.PAGE_FLIP));
        AnimGroup group = new AnimGroup();
        group.action(new SetDepthToTop(layer))
                .then()
                .tweenX(layer)
                .to(0)
                .in(OPEN_CLOSE_ANIM_DURATION)
                .easeIn()
                .then()
                .action(new SetDepthAndUpdateCounter(layer));
        return group.toAnim();
    }

    private void turnToStory() {
        context.game.plat.log().debug("Turning to story");
        iface.anim.add(movePageLeft(reactionPage))
                .then()
                .add(movePageLeft(storyPage));
    }

    private void turnToConclusion() {
        context.game.plat.log().debug("Turning to conclusion");
        iface.anim.add(movePageLeft(skillsPage))
                .then()
                .add(movePageLeft(conclusionPage));
    }

    public RFuture<Void> closeNotebook() {
        final RPromise<Void> promise = RPromise.create();
        depthCounter = 0;
        iface.anim.play(context.game.audioCache.getSound(AudioCache.Key.CLOSE_BOOK))
                .then()
                .add(movePageRight(conclusionPage))
                .then()
                .add(movePageRight(skillsPage))
                .then()
                .add(movePageRight(storyPage))
                .then()
                .add(movePageRight(reactionPage))
                .then()
                .add(movePageRight(encounterPage))
                .then()
                .add(movePageRight(cover))
                .then()
                .action(new Runnable() {
                    @Override
                    public void run() {
                        // Reset depths to fix the problem that 'endPage' never moves, which
                        // breaks my silly depthCounter approach
                        for (int i = 0; i < pages.length; i++) {
                            pages[i].setDepth(pages.length - i);
                        }

                        promise.succeed(null);
                    }
                });
        return promise;
    }

    private Animation movePageRight(final PageLayer layer) {
        final float centerX = width() / 2f;
        AnimGroup group = new AnimGroup();
        group.action(new SetDepthToTop(layer))
                .then()
                .tweenX(layer)
                .to(centerX)
                .in(OPEN_CLOSE_ANIM_DURATION / 4)
                .easeIn()
                .then()
                .action(new SetDepthAndUpdateCounter(layer));
        return group.toAnim();
    }

    private final class SetDepthToTop implements Runnable {
        private final Layer layer;

        private SetDepthToTop(Layer layer) {
            this.layer = checkNotNull(layer);
        }

        @Override
        public void run() {
            layer.setDepth(TOP);
        }
    }

    private final class SetDepthAndUpdateCounter implements Runnable {
        private final Layer layer;

        private SetDepthAndUpdateCounter(Layer layer) {
            this.layer = checkNotNull(layer);
        }

        @Override
        public void run() {
            layer.setDepth(depthCounter);
            depthCounter++;
        }
    }

    private final class NotebookImageLoader {

        private final List<ImageCache.Key> keys = Lists.newLinkedList();
        private ImageCache.Key previousKey = null;

        public NotebookImageLoader() {
            fillKeysList();
        }

        private void fillKeysList() {
            for (ImageCache.Key key : ImageCache.PAGE_KEYS) {
                keys.add(key);
            }
            shuffleUntilFirstItemIsNotTheLastItemReturned();
        }

        private void shuffleUntilFirstItemIsNotTheLastItemReturned() {
            do {
                Shuffler.shuffle(keys);
            } while (keys.get(0) == previousKey);
        }


        private Image next() {
            if (keys.isEmpty()) {
                fillKeysList();
            }
            previousKey = keys.remove(0);
            return context.game.imageCache.image(previousKey);
        }
    }

}
