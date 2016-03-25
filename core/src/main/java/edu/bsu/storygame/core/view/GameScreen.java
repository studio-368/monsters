package edu.bsu.storygame.core.view;

import com.google.common.collect.Maps;
import edu.bsu.storygame.core.MonsterGame;
import playn.core.Game;
import playn.scene.Pointer;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;
import pythagoras.f.IPoint;
import pythagoras.f.Point;
import react.SignalView;
import tripleplay.game.ScreenStack;
import tripleplay.util.Colors;

import java.util.Map;

import static com.google.common.base.Preconditions.*;

public final class GameScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;

    private final Map<NotebookLayer, Point> restingLocations = Maps.newHashMap();


    public GameScreen(MonsterGame game) {
        super(game.plat);
        this.game = game;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();

        final float NOTEBOOK_WIDTH_PERCENT = 0.45f;
        final float NOTEBOOK_GUTTER_WIDTH_PERCENT = 0.60f;
        final float NOTEBOOK_Y_POSITION_PERCENT = 0.75f;

        final IDimension notebookSize = new Dimension(size().width() * NOTEBOOK_WIDTH_PERCENT, size().height());

        final NotebookLayer player1Notebook = new NotebookLayer(Colors.CYAN, notebookSize);
        final NotebookLayer player2Notebook = new NotebookLayer(Colors.YELLOW, notebookSize);

        final float player2NotebookX = (size().width() - size().width() * NOTEBOOK_GUTTER_WIDTH_PERCENT) / 2f;
        final float player1NotebookX = player2NotebookX + (size().width() * (NOTEBOOK_GUTTER_WIDTH_PERCENT - NOTEBOOK_WIDTH_PERCENT));

        final float notebookY = size().height() * NOTEBOOK_Y_POSITION_PERCENT;

        Point notebook1RestingLocation = new Point(player1NotebookX, notebookY);
        Point notebook2RestingLocation = new Point(player2NotebookX, notebookY);
        restingLocations.put(player1Notebook, notebook1RestingLocation);
        restingLocations.put(player2Notebook, notebook2RestingLocation);

        layer.addAt(player2Notebook.layer, notebook2RestingLocation.x, notebook2RestingLocation.y);
        layer.addAt(player1Notebook.layer, notebook1RestingLocation.x, notebook1RestingLocation.y);

        player1Notebook.layer.events().connect(new NotebookOpener(player1Notebook));
        player2Notebook.layer.events().connect(new NotebookOpener(player2Notebook));
    }

    private final class NotebookOpener implements SignalView.Listener<Object> {
        private boolean open = false;

        private final NotebookLayer notebook;

        NotebookOpener(NotebookLayer notebook) {
            this.notebook = checkNotNull(notebook);
        }

        @Override
        public void onEmit(Object o) {
            if (o instanceof Pointer.Interaction) {
                playn.core.Pointer.Event e = ((Pointer.Interaction) o).event;
                if (e.kind == playn.core.Pointer.Event.Kind.END) {
                    if (open) {
                        closeNotebook(notebook);
                        open = false;
                    } else {
                        openNotebook(notebook);
                        open = true;
                    }
                }
            }
        }
    }

    private void openNotebook(NotebookLayer notebook) {
        iface.anim.tweenTranslation(notebook.layer)
                .to(100, 0)
                .in(500f)
                .easeIn();
    }

    private void closeNotebook(NotebookLayer notebook) {
        IPoint target = restingLocations.get(notebook);
        iface.anim.tweenTranslation(notebook.layer)
                .to(target)
                .in(500f)
                .easeIn();
    }

    @Override
    public Game game() {
        return game;
    }
}
