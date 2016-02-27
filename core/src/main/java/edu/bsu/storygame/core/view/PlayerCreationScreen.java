package edu.bsu.storygame.core.view;

import com.google.common.collect.Lists;
import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import edu.bsu.storygame.core.model.Skill;
import playn.core.Game;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;

import java.util.List;

public class PlayerCreationScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;
    private final int MAX_PLAYERS = 2;
    private Root root;
    private Layout layout = AxisLayout.vertical().gap(50);
    private PlayerCreationGroup playerOneGroup;
    private PlayerCreationGroup playerTwoGroup;

    public PlayerCreationScreen(final MonsterGame game) {
        super(game.plat);
        this.game = game;
        root = iface.createRoot(AxisLayout.vertical().gap(80), GameStyle.newSheet(game), layer);
        root.setSize(game.bounds.width(), game.bounds.height());
        root.addStyles(Style.BACKGROUND.is(Background.solid(Colors.ORANGE)));
        root.add(new Label("Nightmare Defenders!"));

        root.add(new Group(AxisLayout.horizontal().gap(100)).add(

                playerOneGroup = new PlayerCreationGroup(layout, game),

                playerTwoGroup = new PlayerCreationGroup(layout, game)));

        final Button startButton = new Button("Start");
        root.add(startButton
                .addStyles(Style.FONT.is(Typeface.OXYGEN.in(game).atSize(0.08f)),
                        Style.HALIGN.center).setEnabled(false)
                .onClick(new Slot<Button>() {
                    @Override
                    public void onEmit(Button button) {
                        game.screenStack.push(new SampleGameScreen(game, createPlayerContext()), game.screenStack.slide());
                    }
                }));

        playerOneGroup.isFilled.connect(new Slot<Boolean>() {
            @Override
            public void onEmit(Boolean isUpdated) {
                if (checkContinuationCondition()) {
                    startButton.setEnabled(true);
                } else {
                    startButton.setEnabled(false);
                }
            }
        });

        playerTwoGroup.isFilled.connect(new Slot<Boolean>() {
            @Override
            public void onEmit(Boolean isUpdated) {
                if (checkContinuationCondition()) {
                    startButton.setEnabled(true);
                } else {
                    startButton.setEnabled(false);
                }
            }
        });
    }

    private Boolean checkContinuationCondition() {
        return playerOneGroup.isFilled.get() && playerTwoGroup.isFilled.get();
    }

    private GameContext createPlayerContext() {

        final List<Skill> skillsOne = Lists.newArrayList();
        final List<Skill> skillsTwo = Lists.newArrayList();

        for (int item = 0; item < MAX_PLAYERS; item++) {
            skillsOne.add(Skill.named(playerOneGroup.selector.selections().get(item).text.get()));
            skillsTwo.add(Skill.named(playerTwoGroup.selector.selections().get(item).text.get()));
        }

        Player playerOne = new Player.Builder().name(playerOneGroup.nameField.text.get())
                .color(Colors.CYAN).skills(skillsOne).build();
        Player playerTwo = new Player.Builder().name(playerTwoGroup.nameField.text.get())
                .color(Colors.RED).skills(skillsTwo).build();

        return new GameContext(game, playerOne, playerTwo);
    }


    @Override
    public Game game() {
        return game;
    }


}


