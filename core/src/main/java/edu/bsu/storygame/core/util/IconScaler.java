package edu.bsu.storygame.core.util;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.ImageCache;
import playn.core.Image;
import tripleplay.ui.Icon;
import tripleplay.ui.Icons;

import static com.google.common.base.Preconditions.*;

public final class IconScaler {

    private final MonsterGame game;

    public IconScaler(MonsterGame game) {
        this.game = checkNotNull(game);
    }

    public Icon scale(ImageCache.Key key, float desiredWidth) {
        final Image image = game.imageCache.image(key);
        final float scale = desiredWidth / image.width();
        return Icons.scaled(Icons.image(image), scale);
    }
}
