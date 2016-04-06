package edu.bsu.storygame.core.view;

import playn.core.Color;

// We keep all of the palette color declarations even if they are unused,
// so that this file matches the palette in the design log.
@SuppressWarnings("unused")
public final class Palette {

    public static final int ROSE = Color.rgb(218, 168, 155);
    public static final int OBSERVATORY = Color.rgb(0, 136, 104);
    public static final int WELL_READ = Color.rgb(140, 53, 65);
    public static final int GOLDEN_POPPY = Color.rgb(240, 197, 0);
    public static final int BLACKCURRANT = Color.rgb(44, 15, 55);
    public static final int NOBEL = Color.rgb(153, 153, 153);
    public static final int BROWN_POD = Color.rgb(66, 33, 11);
    public static final int BLACK = Color.rgb(0, 0, 0);
    public static final int WHITE_SMOKE = Color.rgb(242, 242, 242);
    public static final int SWIRL = Color.rgb(216, 201, 188);

    public static final int PLAYER_ONE = WELL_READ;
    public static final int PLAYER_TWO = BROWN_POD;

    private Palette() {
    }
}
