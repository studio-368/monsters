package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.assets.ImageCache;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Phase;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;
import react.*;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import static com.google.common.base.Preconditions.checkNotNull;

public class MapView extends Group {

    private final GameContext context;
    private final IDimension size;

    public MapView(GameContext gameContext, IDimension size) {
        super(AxisLayout.horizontal());
        this.context = checkNotNull(gameContext);
        this.size = new Dimension(size);
        add(new RegionButton(ImageCache.Key.TRANSPARENT));
    }

    private final class RegionButton extends Button {
        private static final float PERCENT_OF_HEIGHT = 0.8f;

        RegionButton(ImageCache.Key key) {
            Icon rawIcon = Icons.image(context.game.imageCache.image(key));
            float scale = (size.height() * PERCENT_OF_HEIGHT) / rawIcon.height();
            Icon scaledIcon = Icons.scaled(rawIcon, scale);
            this.icon.update(scaledIcon);
            context.phase.connect(new Slot<Phase>() {
                @Override
                public void onEmit(Phase phase) {
                    setEnabled(phase.equals(Phase.MOVEMENT));
                }
            });
            onClick(new SignalView.Listener<Button>() {
                @Override
                public void onEmit(Button button) {
                    context.phase.update(Phase.ENCOUNTER);
                }
            });
        }
    }

}
