package edu.bsu.storygame.core.view;

import com.google.common.collect.Maps;
import edu.bsu.storygame.core.model.*;
import playn.core.Game;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;
import pythagoras.f.IPoint;
import pythagoras.f.Point;
import react.SignalView;
import react.Slot;
import tripleplay.game.ScreenStack;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public final class GameScreen extends ScreenStack.UIScreen {

    private static final float BOOK_TRANSLATION_DURATION = 400f;

    private final GameContext context;
    private final GroupLayer group;

    private final Map<NotebookLayer, Point> restingLocations = Maps.newHashMap();

    public GameScreen(GameContext context) {
        super(context.game.plat);
        this.context = context;
        this.group = new GroupLayer(context.game.bounds.width(), context.game.bounds.height());
        layer.addCenterAt(group, context.game.plat.graphics().viewSize.width() / 2,
                context.game.plat.graphics().viewSize.height() / 2);
    }

    @Override
    public void wasAdded() {
        super.wasAdded();

        initMapView();

        final float width = group.width();
        final float height = group.height();

        final float NOTEBOOK_Y_POSITION_PERCENT = 0.75f;
        final float NOTEBOOK_WIDTH_PERCENT = 0.45f;
        final float NOTEBOOK_HEIGHT_PERCENT = 0.80f;
        final float NOTEBOOK_GUTTER_WIDTH_PERCENT = 0.60f;

        final IDimension notebookSize = new Dimension(width * NOTEBOOK_WIDTH_PERCENT, height * NOTEBOOK_HEIGHT_PERCENT);

        final NotebookLayer player1Notebook = new NotebookLayer(context.players.get(0), notebookSize, context);
        final NotebookLayer player2Notebook = new NotebookLayer(context.players.get(1), notebookSize, context);

        final float player2NotebookX = (width - width * NOTEBOOK_GUTTER_WIDTH_PERCENT) / 2f;
        final float player1NotebookX = player2NotebookX + (width * (NOTEBOOK_GUTTER_WIDTH_PERCENT - NOTEBOOK_WIDTH_PERCENT));

        final float notebookY = height * NOTEBOOK_Y_POSITION_PERCENT;

        Point notebook1RestingLocation = new Point(player1NotebookX, notebookY);
        Point notebook2RestingLocation = new Point(player2NotebookX, notebookY);
        restingLocations.put(player1Notebook, notebook1RestingLocation);
        restingLocations.put(player2Notebook, notebook2RestingLocation);

        HandoffDialogFactory handOff = new HandoffDialogFactory(context);

        group.addAt(player2Notebook, notebook2RestingLocation.x, notebook2RestingLocation.y);
        group.addAt(player1Notebook, notebook1RestingLocation.x, notebook1RestingLocation.y);

        group.add(handOff.create(iface));

        context.phase.connect(new NotebookOpener(player1Notebook, context.players.get(0)));
        context.phase.connect(new NotebookOpener(player2Notebook, context.players.get(1)));
    }

    private void initMapView() {
        MapView mapView = new MapView(context);
        mapView.setOrigin(Layer.Origin.CENTER);
        mapView.setScale(context.game.bounds.width() / mapView.width());
        group.addAt(mapView, context.game.bounds.width() / 2, mapView.scaledHeight() / 2);

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

        NotebookOpener(NotebookLayer notebook, Player player) {
            this.notebook = checkNotNull(notebook);
            this.player = checkNotNull(player);
        }

        @Override
        public void onEmit(Phase phase) {
            if (context.currentPlayer.get().equals(player)) {
                if (phase.equals(Phase.ENCOUNTER)) {
                    openNotebook(notebook);
                }
                if (phase.equals(Phase.END_OF_ROUND)) {
                    closeNotebook(notebook);
                }
            }
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

    private void closeNotebook(final NotebookLayer notebook) {
        IPoint target = restingLocations.get(notebook);
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
    }

    @Override
    public Game game() {
        return context.game;
    }
}
