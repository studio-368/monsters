package edu.bsu.storygame.core.view;

import playn.core.Graphics;
import tripleplay.ui.*;

/**
 * A stylesheet generator.
 * <p/>
 * This is based on TriplePlay's SimpleStyles class.
 *
 * @see <a href="https://github.com/threerings/tripleplay/blob/master/core/src/main/java/tripleplay/ui/SimpleStyles.java">SimpleStyles</a>
 */
public final class GameStyle {

    private GameStyle() {
    }

    /**
     * Creates and returns a simple default stylesheet.
     */
    public static Stylesheet newSheet(Graphics gfx) {
        return newSheetBuilder(gfx).create();
    }

    /**
     * Creates and returns a stylesheet builder configured with some useful default styles. The
     * caller can augment the sheet with additional styles and call {@code create}.
     */
    public static Stylesheet.Builder newSheetBuilder(Graphics gfx) {
        int bgColor = 0xFFCCCCCC, ulColor = 0xFFEEEEEE, brColor = 0xFFAAAAAA;
        Background butBg = Background.roundRect(gfx, bgColor, 5, ulColor, 2).inset(5, 6, 2, 6);
        Background butSelBg = Background.roundRect(gfx, bgColor, 5, brColor, 2).inset(6, 5, 1, 7);
        return Stylesheet.builder().
                add(Button.class,
                        Style.BACKGROUND.is(butBg)).
                add(Button.class, Style.Mode.SELECTED,
                        Style.BACKGROUND.is(butSelBg)).
                add(ToggleButton.class,
                        Style.BACKGROUND.is(butBg)).
                add(ToggleButton.class, Style.Mode.SELECTED,
                        Style.BACKGROUND.is(butSelBg)).
                add(CheckBox.class,
                        Style.BACKGROUND.is(Background.roundRect(gfx, bgColor, 5, ulColor, 2).
                                inset(3, 2, 0, 3))).
                add(CheckBox.class, Style.Mode.SELECTED,
                        Style.BACKGROUND.is(Background.roundRect(gfx, bgColor, 5, brColor, 2).
                                inset(3, 2, 0, 3))).
                // flip ul and br to make Field appear recessed
                        add(Field.class,
                        Style.BACKGROUND.is(Background.beveled(0xFFFFFFFF, brColor, ulColor).inset(5)),
                        Style.HALIGN.left).
                        add(Field.class, Style.Mode.DISABLED,
                                Style.BACKGROUND.is(Background.beveled(0xFFCCCCCC, brColor, ulColor).inset(5))).
                        add(Menu.class,
                                Style.BACKGROUND.is(Background.bordered(0xFFFFFFFF, 0x00000000, 1).inset(6))).
                        add(MenuItem.class,
                                Style.BACKGROUND.is(Background.solid(0xFFFFFFFF)),
                                Style.HALIGN.left).
                        add(MenuItem.class, Style.Mode.SELECTED,
                                Style.BACKGROUND.is(Background.solid(0xFF000000)),
                                Style.COLOR.is(0xFFFFFFFF)).
                        add(Tabs.class,
                                Tabs.HIGHLIGHTER.is(Tabs.textColorHighlighter(0xFF000000, 0xFFFFFFFF)));
    }
}
