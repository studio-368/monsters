package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import playn.core.Font;
import playn.core.Game;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.TableLayout;
import tripleplay.util.Colors;

public class PlayerCreationScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;
    private GameContext context;
    private final BiSelector playerOneSelector;
    private final BiSelector playerTwoSelector;
    private final Group playerOneSkills;
    private final Group playerTwoSkills;
    private final Field playerOneField;
    private final Field playerTwoField;

    public PlayerCreationScreen(final MonsterGame game) {
        super(game.plat);
        this.game = game;
        playerOneSelector = new BiSelector();
        playerTwoSelector = new BiSelector();
        Root root = iface.createRoot(AxisLayout.vertical().gap(100), SimpleStyles.newSheet(game.plat.graphics()), layer);
        root.setSize(game.bounds.width(), game.bounds.height());
        root.addStyles(Style.BACKGROUND.is(Background.solid(Colors.ORANGE)));
        root.add(new Label("Nightmare Defenders!"));
        root.add(new Group(AxisLayout.horizontal().gap(200)).add(
                new Group(AxisLayout.vertical().gap(50)).add(
                        playerOneField = new Field("Name:"),
                        playerOneSkills = new Group(new TableLayout(2).gaps(20, 20)).add(
                                new ToggleButton("Athleticism"),
                                new ToggleButton("Logic"),
                                new ToggleButton("Magic"),
                                new ToggleButton("Persuasion"),
                                new ToggleButton("Stealth"),
                                new ToggleButton("Weapon Use")
                        )
                ),
                new Group(AxisLayout.vertical().gap(50)).add(
                        playerTwoField = new Field("Name:"),
                        playerTwoSkills = new Group(new TableLayout(2).gaps(20, 20)).add(
                                new ToggleButton("Athleticism"),
                                new ToggleButton("Logic"),
                                new ToggleButton("Magic"),
                                new ToggleButton("Persuasion"),
                                new ToggleButton("Stealth"),
                                new ToggleButton("Weapon Use")
                        )
                )
        ));

        linkSelectors();

        root.add(new Button("Start")
                .addStyles(Style.FONT.is(new Font("Times New Roman", 50)),
                        Style.HALIGN.center)
                .onClick(new Slot<Button>() {
                    @Override
                    public void onEmit(Button button) {
                        savePlayerInformation();
                        game.screenStack.push(new SampleGameScreen(game, context), game.screenStack.slide());
                    }
                }));
    }

    private void linkSelectors() {
        for (Element<?> button : playerOneSkills) {
            playerOneSelector.add((ToggleButton) button);
        }
        for (Element<?> button : playerTwoSkills) {
            playerTwoSelector.add((ToggleButton) button);
        }
    }

    private void savePlayerInformation() {
        //playerOneSelector.selections();
        //playerTwoSelector.selections();
        this.context = new GameContext(game, new Player(playerOneField.text.get(), Colors.BLUE), new Player(playerTwoField.text.get(), Colors.CYAN));

    }

    @Override
    public Game game() {
        return game;
    }


}