package edu.bsu.storygame.core.view;


import edu.bsu.storygame.core.model.GameContext;
import playn.core.Color;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import react.SignalView;
import react.Slot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;

import static com.google.common.base.Preconditions.checkNotNull;

public class WinScreen {

    private final GameContext context;

    public WinScreen(GameContext context) {
        this.context = checkNotNull(context);
    }

    public Layer create(Interface iface) {
        final GroupLayer layer = new GroupLayer(context.game.bounds.width() / 2f, context.game.bounds.height() / 2f);
        final Label label = new Label();
        layer.setVisible(false);
        iface.createRoot(AxisLayout.vertical().offStretch(), GameStyle.newSheet(context.game), layer)
                .setSize(layer.width(), layer.height())
                .addStyles(Style.BACKGROUND.is(Background.solid(Color.argb(128, 128, 128, 128))))
                .add(label.addStyles(Style.COLOR.is(Colors.WHITE)))

                .add(new Button("Play Again?").onClick(new Slot<Button>() {
                    @Override
                    public void onEmit(Button button) {
                        context.game.screenStack.push(new PlayerCreationScreen(context.game), context.game.screenStack.slide());
                    }
                }));
        context.currentPlayer.get().hasWon.connect(new SignalView.Listener<Boolean>() {
            @Override
            public void onEmit(Boolean won) {
                if (context.currentPlayer.get().hasWon.get().equals(true)) {
                    label.setText(context.currentPlayer.get().name + " Wins!");
                    layer.setVisible(true);
                }
            }
        });
        return layer;
    }
}
