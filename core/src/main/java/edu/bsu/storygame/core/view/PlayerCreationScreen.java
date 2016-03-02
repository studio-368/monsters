package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import playn.core.Game;
import react.Slot;
import react.Values;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.FlowLayout;

public final class PlayerCreationScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;
    private final Root root;
    private final PlayerCreationGroup playerOneGroup;
    private final PlayerCreationGroup playerTwoGroup;

    public PlayerCreationScreen(final MonsterGame game) {
        super(game.plat);
        this.game = game;
        root = iface.createRoot(AxisLayout.vertical().offStretch(),
                GameStyle.newSheet(game), layer);
        root.setSize(game.bounds.width(), game.bounds.height());
        root.addStyles(Style.BACKGROUND.is(Background.solid(Palette.TUSCANY)));
        root.add(new Label("Nightmare Defenders").addStyles(Style.FONT.is(Typeface.PASSION_ONE.in(game).atSize(0.15f))));
        playerOneGroup = createPlayerGroup(Palette.PLAYER_ONE);
        playerTwoGroup = createPlayerGroup(Palette.PLAYER_TWO);
        root.add(new Group(AxisLayout.horizontal().offStretch().stretchByDefault().gap(0))
                .add(playerOneGroup, playerTwoGroup)
                .setConstraint(Constraints.fixedHeight(game.bounds.percentOfHeight(0.65f)))
        );

        final Button startButton = new StartButton();
        root.add(new Group(new FlowLayout())
                .add(startButton.setEnabled(false)));

        Values.and(playerOneGroup.complete, playerTwoGroup.complete).connect(startButton.enabledSlot());
    }

    private PlayerCreationGroup createPlayerGroup(final int color) {
        PlayerCreationGroup group = new PlayerCreationGroup(game);
        group.addStyles(Style.BACKGROUND.is(Background.solid(color)),
                Style.VALIGN.top);
        return group;
    }

    private GameContext createGameContext() {
        Player p1 = playerOneGroup.createPlayerBuilder().color(Palette.BLUE_LAGOON).build();
        Player p2 = playerTwoGroup.createPlayerBuilder().color(Palette.TROPICAL_RAIN_FOREST).build();
        return new GameContext(game, p1, p2);
    }

    @Override
    public Game game() {
        return game;
    }

    final class StartButton extends Button {
        private StartButton() {
            super("Start");
            onClick(new Slot<Button>() {
                @Override
                public void onEmit(Button button) {
                    game.screenStack.push(new SampleGameScreen(game, createGameContext()), game.screenStack.slide());
                }
            });
        }

        @Override
        protected Class<?> getStyleClass() {
            return StartButton.class;
        }
    }
}


