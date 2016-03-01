package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.Typeface;
import playn.core.Font;
import playn.core.Graphics;
import tripleplay.ui.*;
import tripleplay.ui.util.Insets;
import tripleplay.util.Colors;

/**
 * A stylesheet generator.
 * <p/>
 * This is based on TriplePlay's SimpleStyles class.
 *
 * @see <a href="https://github.com/threerings/tripleplay/blob/master/core/src/main/java/tripleplay/ui/SimpleStyles.java">SimpleStyles</a>
 */
public final class GameStyle {

    private static final float LARGE = 0.05f;
    private static final float REGULAR = 0.038f;
    private static final float SMALL = 0.02f;

    private GameStyle() {
    }

    public static Stylesheet newSheet(MonsterGame game) {
        return newSheetBuilder(game).create();
    }

    public static Stylesheet.Builder newSheetBuilder(MonsterGame game) {
        final Graphics gfx = game.plat.graphics();

        int bgColor = 0xFFCCCCCC, ulColor = 0xFFEEEEEE, brColor = 0xFFAAAAAA;
        Background butBg = Background.roundRect(gfx, bgColor, 5, ulColor, 2).inset(5, 6, 2, 6);
        Background butSelBg = Background.roundRect(gfx, bgColor, 5, brColor, 2).inset(6, 5, 1, 7);

        final float cornerRadius = game.bounds.percentOfHeight(0.02f);
        final float borderWidth = game.bounds.percentOfHeight(0.005f);
        final Insets insets = new Insets(game.bounds.percentOfHeight(0.02f),
                game.bounds.percentOfHeight(0.02f),
                game.bounds.percentOfHeight(0.02f),
                game.bounds.percentOfHeight(0.02f));
        final Background palettizedButtonBg = Background.roundRect(gfx, Palette.BLACK_PEARL, cornerRadius, Palette.SPROUT, borderWidth)
                .insets(insets);
        final Background palettizedSelectedButtonBg = Background.roundRect(gfx, Palette.SPROUT, cornerRadius, Palette.BLACK_PEARL, borderWidth)
                .insets(insets);

        final Font oxygenRegular = Typeface.OXYGEN.in(game).atSize(REGULAR);
        final Font oxygenLarge = Typeface.OXYGEN.in(game).atSize(LARGE);
        final Font oxygenLightRegular = Typeface.OXYGEN_LIGHT.in(game).atSize(REGULAR);

        final Style.Binding buttonRegularStyle = Style.BACKGROUND.is(butBg);
        final Style.Binding buttonSelectedStyle = Style.BACKGROUND.is(butSelBg);

        return Stylesheet.builder()
                .add(Label.class,
                        Style.FONT.is(oxygenRegular))
                .add(Button.class,
                        buttonRegularStyle)
                .add(Button.class, Style.Mode.SELECTED,
                        buttonSelectedStyle)
                .add(ToggleButton.class,
                        Style.BACKGROUND.is(butBg))
                .add(ToggleButton.class, Style.Mode.SELECTED,
                        Style.BACKGROUND.is(butSelBg))
                .add(CheckBox.class,
                        Style.BACKGROUND.is(Background.roundRect(gfx, bgColor, 5, ulColor, 2).
                                inset(3, 2, 0, 3)))
                .add(CheckBox.class, Style.Mode.SELECTED,
                        Style.BACKGROUND.is(Background.roundRect(gfx, bgColor, 5, brColor, 2).
                                inset(3, 2, 0, 3)))
                // flip ul and br to make Field appear recessed
                .add(Field.class,
                        Style.BACKGROUND.is(Background.beveled(0xFFFFFFFF, brColor, ulColor).inset(5)),
                        Style.HALIGN.left)
                .add(Field.class, Style.Mode.DISABLED,
                        Style.BACKGROUND.is(Background.beveled(0xFFCCCCCC, brColor, ulColor).inset(5)))
                .add(Menu.class,
                        Style.BACKGROUND.is(Background.bordered(0xFFFFFFFF, 0x00000000, 1).inset(6)))
                .add(MenuItem.class,
                        Style.BACKGROUND.is(Background.solid(0xFFFFFFFF)),
                        Style.HALIGN.left)
                .add(MenuItem.class, Style.Mode.SELECTED,
                        Style.BACKGROUND.is(Background.solid(0xFF000000)),
                        Style.COLOR.is(0xFFFFFFFF))
                .add(Tabs.class,
                        Tabs.HIGHLIGHTER.is(Tabs.textColorHighlighter(0xFF000000, 0xFFFFFFFF)))

                .add(PlayerCreationGroup.SkillButton.class,
                        Style.FONT.is(oxygenLarge),
                        Style.COLOR.is(Colors.BLACK),
                        Style.BACKGROUND.is(Background.roundRect(gfx, Colors.WHITE, borderWidth, Colors.BLACK, borderWidth)))
                .add(PlayerCreationGroup.SkillButton.class, Style.Mode.SELECTED,
                        Style.FONT.is(oxygenLarge),
                        Style.COLOR.is(Colors.WHITE),
                        Style.BACKGROUND.is(Background.roundRect(gfx, Colors.BLACK, borderWidth, Colors.WHITE, borderWidth)))

                .add(EncounterCardFactory.EncounterCard.class,
                        Style.BACKGROUND.is(Background.roundRect(game.plat.graphics(), Palette.BLACK_PEARL, game.bounds.percentOfHeight(0.03f))))
                .add(EncounterCardFactory.EncounterCard.TitleLabel.class,
                        Style.FONT.is(Typeface.OXYGEN.in(game).atSize(LARGE)),
                        Style.COLOR.is(Palette.TROPICAL_RAIN_FOREST),
                        Style.BACKGROUND.is(Background.blank().inset(game.bounds.percentOfHeight(SMALL))),
                        Style.ICON_GAP.is((int) game.bounds.percentOfHeight(SMALL)),
                        Style.HALIGN.center,
                        Style.ICON_POS.below)
                .add(EncounterCardFactory.EncounterCard.InteractionArea.StyledButton.class,
                        Style.FONT.is(oxygenLarge),
                        Style.BACKGROUND.is(palettizedButtonBg),
                        Style.COLOR.is(Palette.SPROUT))
                .add(EncounterCardFactory.EncounterCard.InteractionArea.StyledButton.class, Style.Mode.SELECTED,
                        Style.FONT.is(oxygenLarge),
                        Style.BACKGROUND.is(palettizedSelectedButtonBg),
                        Style.COLOR.is(Palette.BLACK_PEARL))
                .add(EncounterCardFactory.EncounterCard.InteractionArea.StyledNarrativeLabel.class,
                        Style.TEXT_WRAP.on,
                        Style.FONT.is(oxygenLightRegular),
                        Style.COLOR.is(Palette.SPROUT),
                        Style.BACKGROUND.is(Background.solid(Palette.BLUE_LAGOON)
                                .inset(game.bounds.percentOfHeight(SMALL))))
                .add(EncounterCardFactory.EncounterCard.InteractionArea.SkillTriggerButton.class,
                        Style.COLOR.is(Palette.SPROUT),
                        Style.BACKGROUND.is(palettizedButtonBg))
                .add(EncounterCardFactory.EncounterCard.InteractionArea.SkillTriggerButton.class, Style.Mode.SELECTED,
                        Style.COLOR.is(Palette.BLACK_PEARL),
                        Style.BACKGROUND.is(palettizedSelectedButtonBg))
                .add(EncounterCardFactory.EncounterCard.InteractionArea.RewardLabel.class,
                        Style.COLOR.is(Palette.SPROUT));
    }
}
