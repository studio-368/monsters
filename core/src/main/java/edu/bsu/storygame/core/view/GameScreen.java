package edu.bsu.storygame.core.view;

import com.google.common.collect.Maps;
import edu.bsu.storygame.core.model.*;
import playn.core.Game;
import playn.scene.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;
import pythagoras.f.IPoint;
import pythagoras.f.Point;
import react.*;

import java.util.Map;

import static com.google.common.base.Preconditions.*;

public final class GameScreen extends BoundedUIScreen {

    private static final float BOOK_TRANSLATION_DURATION = 400f;

    private final GameContext context;

    private final Map<Integer, Point> restingLocations = Maps.newHashMap();

    private final NotebookLayer[] notebooks = new NotebookLayer[2];

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

        final float NOTEBOOK_Y_POSITION_PERCENT = 0.75f;
        final float NOTEBOOK_WIDTH_PERCENT = 0.45f;
        final float NOTEBOOK_HEIGHT_PERCENT = 0.80f;
        final float NOTEBOOK_GUTTER_WIDTH_PERCENT = 0.65f;

        final IDimension notebookSize = new Dimension(width * NOTEBOOK_WIDTH_PERCENT, height * NOTEBOOK_HEIGHT_PERCENT);

        final NotebookLayer player1Notebook = new NotebookLayer(context.players.get(0), notebookSize, context);
        final NotebookLayer player2Notebook = new NotebookLayer(context.players.get(1), notebookSize, context);
        notebooks[0] = player1Notebook;
        notebooks[1] = player2Notebook;

        final float player2NotebookX = (width - width * NOTEBOOK_GUTTER_WIDTH_PERCENT) / 2f;
        final float player1NotebookX = player2NotebookX + (width * (NOTEBOOK_GUTTER_WIDTH_PERCENT - NOTEBOOK_WIDTH_PERCENT));

        final float notebookY = height * NOTEBOOK_Y_POSITION_PERCENT;

        Point notebook1RestingLocation = new Point(player1NotebookX, notebookY);
        Point notebook2RestingLocation = new Point(player2NotebookX, notebookY);
        restingLocations.put(0, notebook1RestingLocation);
        restingLocations.put(1, notebook2RestingLocation);

        content.addAt(player2Notebook, notebook2RestingLocation.x, notebook2RestingLocation.y);
        content.addAt(player1Notebook, notebook1RestingLocation.x, notebook1RestingLocation.y);

        context.phase.connect(new NotebookOpener(player1Notebook, context.players.get(0)));
        context.phase.connect(new NotebookOpener(player2Notebook, context.players.get(1)));

        HandoffDialogFactory handOff = new HandoffDialogFactory(context);
        content.addCenterAt(handOff.create(iface), content.width() / 2, content.height() / 2);
    }

    private void initMapView() {
        MapView mapView = new MapView(context);
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
                        notebook.open(iface.anim);
                    }
                });
    }

    private RFuture<Void> closeNotebook(final NotebookLayer notebook) {
        final RPromise<Void> promise = RPromise.create();
        final NotebookLayer newCurrentNotebook = (notebook == notebooks[0]) ? notebooks[1] : notebooks[0];
        IPoint target = restingLocations.get(1);
        iface.anim.action(new Runnable() {
            @Override
            public void run() {
                notebook.close(iface.anim);
            }
        }).then()
                .delay(NotebookLayer.OPEN_CLOSE_ANIM_DURATION)
                .then()
                .tweenTranslation(notebook)
                .to(target)
                .in(BOOK_TRANSLATION_DURATION)
                .easeIn();
        final IPoint newTarget = restingLocations.get(0);
        iface.anim.tweenTranslation(newCurrentNotebook)
                .to(newTarget)
                .in(BOOK_TRANSLATION_DURATION)
                .easeIn().then().action(new Runnable() {
            @Override
            public void run() {
                // Change Z-order.
                // TODO: handle this more elegantly by changing layer depths.
                content.remove(newCurrentNotebook);
                content.addAt(newCurrentNotebook, newTarget.x(), newTarget.y());

                promise.succeed(null);
            }
        });
        return promise;
    }

    @Override
    public Game game() {
        return context.game;
    }
}
