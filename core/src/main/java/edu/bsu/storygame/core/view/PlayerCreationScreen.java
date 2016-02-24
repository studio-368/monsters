package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import playn.core.Font;
import playn.core.Game;
import react.RList;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.TableLayout;
import tripleplay.util.Colors;

import java.util.ArrayList;

public class PlayerCreationScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;
    private GameContext context;
    private final BiSelector playerOneSelector;
    private final BiSelector playerTwoSelector;
    private final Group playerOneSkills;
    private final Group playerTwoSkills;
    private final Field playerOneField;
    private final Field playerTwoField;
    final RList<String> skillsOne = new RList<>(new ArrayList<String>());
    final RList<String> skillsTwo = new RList<>(new ArrayList<String>());
    private Root root;
    private Label header;

    public PlayerCreationScreen(final MonsterGame game) {
        super(game.plat);
        this.game = game;
        playerOneSelector = new BiSelector();
        playerTwoSelector = new BiSelector();
        root = iface.createRoot(AxisLayout.vertical().gap(100), SimpleStyles.newSheet(game.plat.graphics()), layer);
        root.setSize(game.bounds.width(), game.bounds.height());
        root.addStyles(Style.BACKGROUND.is(Background.solid(Colors.ORANGE)));
        root.add(header = new Label("Nightmare Defenders!"));
        root.add(new Group(AxisLayout.horizontal().gap(200)).add(
                new Group(AxisLayout.vertical().gap(50)).add(
                        playerOneField = new Field("Name").setPopupLabel("Enter Your Name"),
                        playerOneSkills = new Group(new TableLayout(2).gaps(20, 20)).add(
                                new SkillButton("Athleticism"),
                                new SkillButton("Logic"),
                                new SkillButton("Magic"),
                                new SkillButton("Persuasion"),
                                new SkillButton("Stealth"),
                                new SkillButton("Weapon Use")
                        )
                ),
                new Group(AxisLayout.vertical().gap(50)).add(
                        playerTwoField = new Field("Name").setPopupLabel("Enter Your Name"),
                        playerTwoSkills = new Group(new TableLayout(2).gaps(20, 20)).add(
                                new SkillButton("Athleticism"),
                                new SkillButton("Logic"),
                                new SkillButton("Magic"),
                                new SkillButton("Persuasion"),
                                new SkillButton("Stealth"),
                                new SkillButton("Weapon Use")
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
                        if (hasCompletedPlayerCreation()) {
                            savePlayerInformation();
                            game.screenStack.push(new SampleGameScreen(game, context), game.screenStack.slide());
                        } else {
                            header.setText("Error");
                        }
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
        for (int item = 0; item < 2; item++) {
            skillsOne.add(playerOneSelector.selections().get(item).text.get());
            skillsTwo.add(playerOneSelector.selections().get(item).text.get());
        }
        this.context = new GameContext(game, new Player(playerOneField.text.get(), Colors.BLUE, skillsOne), new Player(playerTwoField.text.get(), Colors.CYAN, skillsTwo));
    }

    private boolean hasCompletedPlayerCreation() {
        return playerOneSelector.selections().size() == 2
                && playerTwoSelector.selections().size() == 2
                && !playerOneField.text.equals(null)
                && !playerTwoField.text.equals(null);
    }

    @Override
    public Game game() {
        return game;
    }

    private final class SkillButton extends ToggleButton {

        private SkillButton(String text) {
            super(text);
            setConstraint(Constraints.fixedSize(game.bounds.width() * 0.1f, game.bounds.height() * 0.1f));
        }
    }


}