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
    private String[] players;
    private final Button startButton;

    public PlayerCreationScreen(final MonsterGame game, String[] players) {
        super(game.plat);
        this.game = game;
        this.players = players;
        root = iface.createRoot(AxisLayout.vertical().offStretch(),
                GameStyle.newSheet(game), layer);
        root.setSize(game.bounds.width(), game.bounds.height());
        root.addStyles(Style.BACKGROUND.is(Background.solid(Palette.TUSCANY)));
        root.add(new Label("Traveler's Notebook: Monster Tales").addStyles(Style.FONT.is(Typeface.PASSION_ONE.in(game).atSize(0.10f))));
        root.add(new Label("Please customize your characters:").addStyles(Style.FONT.is(Typeface.PASSION_ONE.in(game).atSize(0.05f))));
        playerOneGroup = createPlayerGroup(Palette.PLAYER_ONE, players[0]);
        playerTwoGroup = createPlayerGroup(Palette.PLAYER_TWO, players[1]);
        root.add(new Group(AxisLayout.horizontal().offStretch().stretchByDefault().gap(0))
                .add(playerOneGroup, playerTwoGroup)
                .setConstraint(Constraints.fixedHeight(game.bounds.percentOfHeight(0.65f)))
        );
        startButton = new Button("Start");
        startButton.onClick(new Slot<Button>() {
            @Override
            public void onEmit(Button button) {
                game.screenStack.push(new GameScreen(createGameContext()), game.screenStack.slide());
            }
        });
        root.add(new Group(new FlowLayout())
                .add(startButton.setEnabled(false)));

       Values.and(playerOneGroup.complete, playerTwoGroup.complete).connect(startButton.enabledSlot());
    }

    private PlayerCreationGroup createPlayerGroup(final int color, String playerName) {
        PlayerCreationGroup group = new PlayerCreationGroup(game, playerName);
        group.addStyles(Style.BACKGROUND.is(Background.solid(color)),
                Style.VALIGN.top);
        return group;
    }

    private GameContext createGameContext(){

        Player p1 = new Player.Builder().name(players[0]).color(Palette.PLAYER_ONE).skills(playerOneGroup.getSelectedSkills()).build();
        Player p2 = new Player.Builder().name(players[1]).color(Palette.PLAYER_TWO).skills(playerTwoGroup.getSelectedSkills()).build();
        return new GameContext(game, p1, p2);
    }

    @Override
    public Game game() {
        return game;
    }
}
