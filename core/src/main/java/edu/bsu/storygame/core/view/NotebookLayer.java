package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;
import react.Slot;
import tripleplay.anim.Animator;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
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
            color = (player == context.players.get(0)) ? Colors.GREEN : Colors.CYAN;
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
                    new Shim(0, 0).setConstraint(AxisLayout.stretched()));
        }
    }

    private final class EncounterPage extends PageLayer {
        private EncounterPage() {
            Root root = iface.createRoot(AxisLayout.vertical(), stylesheet, this)
                    .setSize(closedSize)
                    .addStyles(Style.BACKGROUND.is(Background.solid(color)));
            root.add(new Label("Notes sticky goes here"),
                    new Label("Encounter picture goes here like in old card design")
                            .addStyles(Style.TEXT_WRAP.on));
        }
    }

    private final class ReactionPage extends PageLayer {
        private ReactionPage() {
            Root root = iface.createRoot(AxisLayout.vertical(), stylesheet, this)
                    .setSize(closedSize)
                    .addStyles(Style.BACKGROUND.is(Background.solid(color)));
            root.add(new Label("Reaction options go here"));
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