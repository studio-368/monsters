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
    private final BiSelector playerOneSelector = new BiSelector();
    private final BiSelector playerTwoSelector = new BiSelector();
    private playerOneGroup groupFactory = new playerOneGroup();
    private final Group playerOneSkills;
    private final Group playerTwoSkills;
    private final Field playerOneField;
    private final Field playerTwoField;
    final RList<String> skillsOne = new RList<>(new ArrayList<String>());
    final RList<String> skillsTwo = new RList<>(new ArrayList<String>());
    private Label header;
    private Root root;

    public PlayerCreationScreen(final MonsterGame game) {
        super(game.plat);
        this.game = game;
        root = iface.createRoot(AxisLayout.vertical().gap(100), SimpleStyles.newSheet(game.plat.graphics()), layer);
        root.setSize(game.bounds.width(), game.bounds.height());
        root.addStyles(Style.BACKGROUND.is(Background.solid(Colors.ORANGE)));
        root.add(header = new Label("Nightmare Defenders!"));
        root.add(new Group(AxisLayout.horizontal().gap(200)).add(
                groupFactory.createPlayerGroup(playerOneField = new Field("Name"), playerOneSkills = groupFactory.createSkillGroup()),
                groupFactory.createPlayerGroup(playerTwoField = new Field("Name"), playerTwoSkills = groupFactory.createSkillGroup())
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
            skillsTwo.add(playerTwoSelector.selections().get(item).text.get());
        }
        this.context = new GameContext(game, new Player(playerOneField.text.get(), Colors.BLUE, skillsOne), new Player(playerTwoField.text.get(), Colors.CYAN, skillsTwo));
    }

    private boolean hasCompletedPlayerCreation() {
        return playerOneSelector.selections().size() == 2
                && playerTwoSelector.selections().size() == 2
                && !playerOneField.text.get().equals("Name")
                && !playerTwoField.text.get().equals("Name");
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

    private final class playerOneGroup extends PlayerCreationGroup {

        @Override
        public Group createPlayerGroup(Field nameField, Group skillGroup) {

            return new Group(AxisLayout.vertical().gap(50)).add(
                    nameField.setPopupLabel("Enter Your Name"),
                    skillGroup.add(new SkillButton("Athleticism"),
                            new SkillButton("Logic"),
                            new SkillButton("Magic"),
                            new SkillButton("Persuasion"),
                            new SkillButton("Stealth"),
                            new SkillButton("Weapon Use")
                    ));
        }

        @Override
        public Group createSkillGroup() {
            return new Group(new TableLayout(2).gaps(20, 20));
        }
    }


}