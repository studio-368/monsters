package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Phase;
import playn.core.Color;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import react.Slot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;

import static com.google.common.base.Preconditions.checkNotNull;

public class HandoffDialogFactory {

    private final GameContext context;

    public HandoffDialogFactory(GameContext context) {
        this.context = checkNotNull(context);
    }

    public Layer create(Interface iface) {
        final GroupLayer layer = new GroupLayer(context.game.bounds.width() / 2f, context.game.bounds.height() / 2f);
        final Label label = new Label();
        layer.setVisible(false);
        iface.createRoot(AxisLayout.vertical().offStretch(), GameStyle.newSheet(context.game), layer)
                .setSize(layer.width(), layer.height())
                .addStyles(Style.BACKGROUND.is(Background.solid(Color.argb(128, 128, 128, 128))))
                .add(label.addStyles(Style.COLOR.is(Colors.WHITE)),
                        new Button("OK").onClick(new Slot<Button>() {
                            @Override
                            public void onEmit(Button button) {
                                context.phase.update(Phase.STORY);
                            }
                        }));
        context.phase.connect(new Slot<Phase>() {
            @Override
            public void onEmit(Phase phase) {
                if (phase.equals(Phase.HANDOFF)) {
                    final String otherPlayerName = context.otherPlayer().name;
                    label.text.update("Hand the device to " + otherPlayerName);
                }
                layer.setVisible(phase.equals(Phase.HANDOFF));
            }
        });
        return layer;
    }
}
