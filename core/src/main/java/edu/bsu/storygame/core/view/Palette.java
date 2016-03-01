package edu.bsu.storygame.core.view;

import playn.core.Color;

// We keep all of the palette color declarations even if they are unused,
// so that this file matches the palette in the design log.
@SuppressWarnings("unused")
public final class Palette {

    public static final int TROPICAL_RAIN_FOREST = Color.rgb(0, 116, 93);
    public static final int BLACK_PEARL = Color.rgb(2, 17, 24);
    public static final int COCOA_BROWN = Color.rgb(42, 21, 0);
    public static final int JADE = Color.rgb(0, 188, 101);
    public static final int BLUE_LAGOON = Color.rgb(1, 78, 104);
    public static final int NEW_AMBER = Color.rgb(107, 55, 33);
    public static final int SPROUT = Color.rgb(173, 205, 98);
    public static final int CARMINE = Color.rgb(36, 154, 200);
    public static final int TUSCANY = Color.rgb(172, 92, 65);

    public static final int PLAYER_ONE = BLUE_LAGOON;
    public static final int PLAYER_TWO = TROPICAL_RAIN_FOREST;

    private Palette() {
    }
}
