package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Phase;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;
import react.Slot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class HandoffDialogFactory {

    private final GameContext context;

    public HandoffDialogFactory(GameContext context) {
        this.context = checkNotNull(context);
    }

    public Layer create(final Interface iface) {
        final GroupLayer layer = new GroupLayer(context.game.bounds.width(), context.game.bounds.height());
        layer.setVisible(false);

        final HandoffDialog dialog = new HandoffDialog();
        dialog.setConstraint(Constraints.fixedSize(layer.width() / 2f, layer.height() / 2f));

        iface.createRoot(AxisLayout.vertical(), GameStyle.newSheet(context.game), layer)
                .setSize(layer.width(), layer.height())
                .add(dialog);
        context.phase.connect(new Slot<Phase>() {
            @Override
            public void onEmit(Phase phase) {
                if (phase.equals(Phase.HANDOFF)) {
                    final String otherPlayerName = context.otherPlayer().name;
                    dialog.label.text.update("Please pass to " + otherPlayerName + ", who will read you your story!");
                    layer.setVisible(true);
                    iface.anim.tweenY(layer)
                            .from(context.game.bounds.height())
                            .to(0)
                            .in(500f)
                            .easeIn();
                } else if (phase.equals(Phase.STORY)) {
                    iface.anim.tweenY(layer)
                            .from(0)
                            .to(context.game.bounds.height())
                            .easeOut();
                }
                ensureLayerIsOnTop();
            }

            private void ensureLayerIsOnTop() {
                layer.setDepth(100000);
            }
        });

        createMonsterHandIn(layer);
        return layer;
    }

    private void createMonsterHandIn(GroupLayer layer) {
        final ImageLayer monsterHand = new ImageLayer(context.game.imageCache.image(ImageCache.Key.MONSTER_HAND));
        final float handX = context.game.bounds.width() * 3 / 5;
        final float handY = context.game.bounds.height() * 3 / 5;
        final float scale = (context.game.bounds.height() - handY) / monsterHand.height();
        monsterHand.setScale(scale);
        layer.addAt(monsterHand, handX, handY);
    }

    final class HandoffDialog extends Group {
        final Label label = new Label("")
                .addStyles(Style.TEXT_WRAP.on,
                        Style.BACKGROUND.is(Background.blank().inset(
                                context.game.bounds.percentOfHeight(0.08f), 0)));

        private HandoffDialog() {
            super(AxisLayout.vertical().stretchByDefault());
            add(new Shim(0, 0),
                    label.setConstraint(AxisLayout.stretched()),
                    new OkButton().setConstraint(Constraints.fixedWidth(context.game.bounds.width() * 0.20f)),
                    new Shim(0, 0));
        }

        @Override
        protected Class<?> getStyleClass() {
            return HandoffDialog.class;
        }
    }

    final class OkButton extends Button {
        private OkButton() {
            super("Okay");
            onClick(new Slot<Button>() {
                @Override
                public void onEmit(Button button) {
                    context.phase.update(Phase.STORY);
                }
            });
        }

        @Override
        protected Class<?> getStyleClass() {
            return OkButton.class;
        }
    }
}
