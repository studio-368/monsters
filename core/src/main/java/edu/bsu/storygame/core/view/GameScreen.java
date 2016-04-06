package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.intro.SlideData;
import edu.bsu.storygame.core.intro.SlideShow;
import edu.bsu.storygame.core.model.*;
import playn.core.Game;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;
import pythagoras.f.IPoint;
import pythagoras.f.Point;
import react.*;
import tripleplay.anim.AnimGroup;
import tripleplay.ui.Background;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public final class GameScreen extends BoundedUIScreen {

    private static final float BOOK_TRANSLATION_DURATION = 400f;

    private static final float NOTEBOOK_Y_PERCENT = 0.75f;
    private static final float REAR_NOTEBOOK_X_PERCENT = 0.10f;
    private static final float FRONT_NOTEBOOK_X_PERCENT = 0.45f;
    private static final float REAR_NOTEBOOK_DEPTH = 2;
    private static final float FRONT_NOTEBOOK_DEPTH = 4;

    private final GameContext context;

    private NotebookLayer player1Notebook;
    private NotebookLayer player2Notebook;

    /**
     * The notebook's resting y position.
     * <p/>
     * This is semantically a constant, but it cannot be final since this cannot be known until {@link #wasAdded()}}
     * is called. Hence, it should be written once and then only read.
     */
    private float notebookY;

    public GameScreen(GameContext context) {
        super(context.game);
        this.context = context;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();

        initMapView();

        final float width = content.width();
        final float height = content.height();

        final float NOTEBOOK_WIDTH_PERCENT = 0.45f;
        final float NOTEBOOK_HEIGHT_PERCENT = 0.80f;

        final IDimension notebookSize = new Dimension(width * NOTEBOOK_WIDTH_PERCENT, height * NOTEBOOK_HEIGHT_PERCENT);

        player1Notebook = new NotebookLayer(context.players.get(0), notebookSize, context);
        player2Notebook = new NotebookLayer(context.players.get(1), notebookSize, context);

        player1Notebook.setDepth(FRONT_NOTEBOOK_DEPTH);
        player2Notebook.setDepth(REAR_NOTEBOOK_DEPTH);

        final float player2NotebookX = content.width() * REAR_NOTEBOOK_X_PERCENT;
        final float player1NotebookX = content.width() * FRONT_NOTEBOOK_X_PERCENT;

        notebookY = height * NOTEBOOK_Y_PERCENT;

        content.addAt(player2Notebook, player2NotebookX, notebookY);
        content.addAt(player1Notebook, player1NotebookX, notebookY);

        context.phase.connect(new NotebookOpener(player1Notebook, context.players.get(0)));
        context.phase.connect(new NotebookOpener(player2Notebook, context.players.get(1)));

        HandoffDialogFactory handOff = new HandoffDialogFactory(context);
        content.addCenterAt(handOff.create(iface), content.width() / 2, content.height() / 2);

        watchForPlayerWinCondition();
        configureMovementBanner();
    }

    private void watchForPlayerWinCondition() {
        context.currentPlayer.get().storyPoints.connect(new Slot<Integer>() {
            @Override
            public void onEmit(Integer totalPoints) {
                if (totalPoints == context.pointsRequiredForVictory) {
                    SlideShow winShow = new SlideShow(context.game,
                            SlideData.text("Congratulations, " + context.currentPlayer.get().name + "!"),
                            SlideData.text("You sure did find a lot of cool stories. I'll bet your new book will be amazing!")
                                    .imageKey(ImageCache.Key.MISSING_IMAGE)
                    );
                    winShow.startOn(context.game.screenStack).onComplete(new SignalView.Listener<Try<Void>>() {
                        @Override
                        public void onEmit(Try<Void> event) {
                            context.game.screenStack.push(new PlayAgainScreen(context.game), context.game.screenStack.slide());
                        }
                    });
                }
            }
        });
    }

    private void initMapView() {
        MapView mapView = new MapView(context, iface.anim);
        mapView.setOrigin(Layer.Origin.CENTER);
        mapView.setScale(context.game.bounds.width() / mapView.width());
        content.addAt(mapView, context.game.bounds.width() / 2, mapView.scaledHeight() / 2);

        mapView.pick.connect(new Slot<Region>() {
            @Override
            public void onEmit(Region region) {
                context.game.plat.log().debug("Picked " + region);
                initEncounter(region);
            }
        });
    }

    private void configureMovementBanner() {
        MovementPrompt movementPrompt = new MovementPrompt();
        movementPrompt.setOrigin(Layer.Origin.TC);
        content.addFloorAt(movementPrompt, content.width() / 2, 0);
    }

    private void initEncounter(Region region) {
        context.currentPlayer.get().location.update(region);
        Encounter encounter = context.game.narrativeCache.state.result().get().forRegion(region).chooseOne();
        context.encounter.update(encounter);
        context.phase.update(Phase.ENCOUNTER);
    }

    private final class NotebookOpener implements SignalView.Listener<Phase> {

        private final NotebookLayer notebook;
        private final Player player;

        NotebookOpener(final NotebookLayer notebook, Player player) {
            this.notebook = checkNotNull(notebook);
            this.player = checkNotNull(player);

            notebook.onDone.connect(new Slot<Void>() {
                @Override
                public void onEmit(Void aVoid) {
                    closeNotebook(notebook).onComplete(new Slot<Try<Void>>() {
                        @Override
                        public void onEmit(Try<Void> voidTry) {
                            context.phase.update(Phase.END_OF_ROUND);
                        }
                    });
                }
            });
        }

        @Override
        public void onEmit(Phase phase) {
            if (isItMyEncounterPhase()) {
                openNotebook(notebook);
            }
        }

        private boolean isItMyEncounterPhase() {
            return context.currentPlayer.get().equals(player)
                    && context.phase.get() == Phase.ENCOUNTER;
        }
    }

    private void openNotebook(final NotebookLayer notebook) {
        iface.anim.tweenTranslation(notebook)
                .to(context.game.bounds.width() / 2, context.game.bounds.height() * 0.10f)
                .in(BOOK_TRANSLATION_DURATION)
                .easeIn()
                .then()
                .action(new Runnable() {
                    @Override
                    public void run() {
                        notebook.open();
                    }
                });
    }

    private RFuture<Void> closeNotebook(final NotebookLayer notebook) {
        final NotebookLayer otherNotebook = notebook == player1Notebook ? player2Notebook : player1Notebook;
        animateRearNotebookToFront(otherNotebook, notebook);
        return animateNotebookCloseAndDropToRear(notebook);
    }

    private RFuture<Void> animateNotebookCloseAndDropToRear(final NotebookLayer notebook) {
        final RPromise<Void> promise = RPromise.create();
        IPoint target = new Point(content.width() * REAR_NOTEBOOK_X_PERCENT, notebookY);
        iface.anim.action(new Runnable() {
            @Override
            public void run() {
                notebook.close();
            }
        }).then()
                .delay(NotebookLayer.OPEN_CLOSE_ANIM_DURATION)
                .then()
                .tweenTranslation(notebook)
                .to(target)
                .in(BOOK_TRANSLATION_DURATION)
                .easeIn()
                .then()
                .action(new Runnable() {
                    @Override
                    public void run() {
                        promise.succeed(null);
                    }
                });
        return promise;
    }

    private void animateRearNotebookToFront(final NotebookLayer rear, final NotebookLayer front) {
        final float dipAmount = content.height() * 0.18f;
        AnimGroup group = new AnimGroup();
        group.tweenX(rear)
                .to(content.width() * FRONT_NOTEBOOK_X_PERCENT)
                .in(BOOK_TRANSLATION_DURATION);
        group.tweenY(rear)
                .from(notebookY)
                .to(notebookY + dipAmount)
                .in(BOOK_TRANSLATION_DURATION / 2)
                .easeOut()
                .then()
                .action(new Runnable() {
                    @Override
                    public void run() {
                        front.setDepth(REAR_NOTEBOOK_DEPTH);
                        rear.setDepth(FRONT_NOTEBOOK_DEPTH);
                    }
                })
                .then()
                .tweenY(rear)
                .to(notebookY)
                .in(BOOK_TRANSLATION_DURATION / 2)
                .easeIn();
        iface.anim.add(group.toAnim());
    }

    @Override
    public Game game() {
        return context.game;
    }

    private final class MovementPrompt extends GroupLayer {
        private static final String TEXT_STUB = ", pick a place!";
        private static final int TRANSLUCENT_RED = 0xaae64650;
        private final Label label = new Label(context.currentPlayer.get().name + TEXT_STUB);

        private MovementPrompt() {
            super(content.width() * 0.75f, content.height() * 0.10f);
            iface.createRoot(AxisLayout.vertical(), GameStyle.newSheet(context.game), this)
                    .setSize(width(), height())
                    .addStyles(Style.BACKGROUND.is(Background.solid(TRANSLUCENT_RED)))
                    .add(label);
            context.currentPlayer.connect(new Slot<Player>() {
                @Override
                public void onEmit(Player player) {
                    label.text.update(player.name + TEXT_STUB);
                }
            });
            context.phase.connect(new Slot<Phase>() {
                @Override
                public void onEmit(Phase phase) {
                    if (phase == Phase.MOVEMENT) {
                        iface.anim.tweenY(MovementPrompt.this)
                                .from(-height())
                                .to(0)
                                .in(500f);
                    } else if (phase == Phase.ENCOUNTER) {
                        iface.anim.tweenY(MovementPrompt.this)
                                .to(-height())
                                .in(500f);
                    }
                }
            });
        }
    }
}
