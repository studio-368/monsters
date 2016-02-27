package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.Typeface;
import playn.core.Color;
import playn.core.Font;
import playn.core.Graphics;
import tripleplay.ui.*;
import tripleplay.util.Colors;

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

    public static Stylesheet newSheet(MonsterGame game) {
        return newSheetBuilder(game).create();
    }

    public static Stylesheet.Builder newSheetBuilder(MonsterGame game) {
        final Graphics gfx = game.plat.graphics();
        final Font font = Typeface.OXYGEN.in(game).atSize(0.05f);

        int bgColor = 0xFFCCCCCC, ulColor = 0xFFEEEEEE, brColor = 0xFFAAAAAA;
        Background butBg = Background.roundRect(gfx, bgColor, 5, ulColor, 2).inset(5, 6, 2, 6);
        Background butSelBg = Background.roundRect(gfx, bgColor, 5, brColor, 2).inset(6, 5, 1, 7);

        final Style.Binding buttonRegularStyle = Style.BACKGROUND.is(butBg);
        final Style.Binding buttonSelectedStyle = Style.BACKGROUND.is(butSelBg);

        return Stylesheet.builder()
                .add(Label.class,
                        Style.FONT.is(font))
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
                .add(EncounterCardFactory.EncounterCard.class,
                        Style.BACKGROUND.is(Background.solid(Color.rgb(39, 80, 5))))
                .add(EncounterCardFactory.EncounterCard.TitleLabel.class,
                        Style.COLOR.is(Colors.WHITE),
                        Style.HALIGN.center,
                        Style.ICON_POS.below)
                .add(EncounterCardFactory.EncounterCard.InteractionArea.StoryLabel.class,
                        Style.TEXT_WRAP.on,
                        Style.COLOR.is(Colors.WHITE))
                .add(EncounterCardFactory.EncounterCard.InteractionArea.ConclusionLabel.class,
                        Style.TEXT_WRAP.on,
                        Style.COLOR.is(Colors.WHITE))
                .add(EncounterCardFactory.EncounterCard.InteractionArea.SkillTriggerButton.class,
                        buttonRegularStyle)
                .add(EncounterCardFactory.EncounterCard.InteractionArea.SkillTriggerButton.class, Style.Mode.SELECTED,
                        buttonSelectedStyle);
    }
}
