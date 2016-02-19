package edu.bsu.storygame.core.assets;

import edu.bsu.storygame.core.MonsterGame;
import playn.core.Font;

import static com.google.common.base.Preconditions.*;

public enum Typeface {

    OXYGEN(FontConstants.OXYGEN_NAME),
    OXYGEN_LIGHT(FontConstants.OXYGEN_LIGHT_NAME),
    PASSION_ONE(FontConstants.PASSION_ONE_NAME);

    /**
     * An arbitrary font size.
     * The expectation is that consumers of {@link #font} will use {@link Font#derive(float)}} to make their
     * own sized instance.
     */
    private static final float ARBITRARY_SIZE = 12f;

    public final Font font;

    Typeface(String name) {
        this.font = new Font(name, ARBITRARY_SIZE);
    }

    public FontBuilder in(MonsterGame game) {
        return new FontBuilder(game);
    }

    public final class FontBuilder {
        private final MonsterGame game;
        private FontBuilder(MonsterGame game) {
            this.game = checkNotNull(game);
        }
        public Font atSize(float percentOfHeight) {
            return font.derive(game.bounds.percentOfHeight(percentOfHeight));
        }
    }
}
