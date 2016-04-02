package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.Typeface;
import playn.core.Game;
import react.Slot;
import react.Value;
import react.ValueView;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.FlowLayout;

public class PlayerNameScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;
    private final Root root;
    private Field nameFieldOne;
    private Field nameFieldTwo;
    public final ValueView<Boolean> complete = Value.create(false);
    private Button continueButton = new Button("Done");

    public PlayerNameScreen(final MonsterGame game) {
        super(game.plat);
        this.game = game;
        root = iface.createRoot(AxisLayout.vertical().offStretch(),
                GameStyle.newSheet(game), layer);
        root.setSize(game.bounds.width(), game.bounds.height());
        root.addStyles(Style.BACKGROUND.is(Background.solid(Palette.TUSCANY)));
        root.add(new Label("Traveler's Notebook: Monster Tales").addStyles(Style.FONT.is(Typeface.PASSION_ONE.in(game).atSize(0.10f))));
        root.add(new Label("Please enter your names:").addStyles(Style.FONT.is(Typeface.PASSION_ONE.in(game).atSize(0.05f))));
        continueButton.onClick(new Slot<Button>() {
            @Override
            public void onEmit(Button button) {
                game.screenStack.push(new PlayerCreationScreen(game, new String[]{
                        nameFieldOne.text.get().trim(), nameFieldTwo.text.get().trim()
                }), game.screenStack.slide());
            }
        });
        root.add(new Group(AxisLayout.horizontal().offStretch().stretchByDefault().gap(0))
                .add(createNameArea(1), createNameArea(2))
                .setConstraint(Constraints.fixedHeight(game.bounds.percentOfHeight(0.65f))));
        root.add(new Group(new FlowLayout())
                .add(continueButton.setEnabled(false)));
    }

    private Group createNameArea(int player) {
        Group group = new Group(AxisLayout.vertical());
        group.add(new Label("Player " + player + ":"));
        int color = Palette.PLAYER_TWO;
        if (player == 1) {
            color = Palette.PLAYER_ONE;
            group.add(nameFieldOne = new Field()
                    .setConstraint(Constraints.fixedSize(game.bounds.width() * 0.10f, game.bounds.height() * 0.08f)));
            nameFieldOne.text.connect(new Slot<String>() {
                @Override
                public void onEmit(String s) {
                    checkForCompletion();
                }
            });
        } else {
            group.add(nameFieldTwo = new Field()
                    .setConstraint(Constraints.fixedSize(game.bounds.width() * 0.10f, game.bounds.height() * 0.08f)));
            nameFieldTwo.text.connect(new Slot<String>() {
                @Override
                public void onEmit(String s) {
                    checkForCompletion();
                }
            });
        }
        group.addStyles(Style.BACKGROUND.is(Background.solid(color)));
        return group;
    }

    private void checkForCompletion() {
        final boolean isComplete = !nameFieldOne.text.get().trim().isEmpty() && !nameFieldTwo.text.get().trim().isEmpty();
        ((Value<Boolean>) complete).update(isComplete);
        if (complete.get()) {
            continueButton.setEnabled(true);
        }
    }

    @Override
    public Game game() {
        return game;
    }
}
