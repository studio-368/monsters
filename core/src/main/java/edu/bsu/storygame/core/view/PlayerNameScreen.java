package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.model.Player;
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
    private Field nameField;
    public final ValueView<Boolean> complete = Value.create(false);
    private PlayerCreationScreen creationScreen;

    public PlayerNameScreen(final MonsterGame game) {
        super(game.plat);
        this.game = game;
        root = iface.createRoot(AxisLayout.vertical().offStretch(),
                GameStyle.newSheet(game), layer);
        root.setSize(game.bounds.width(), game.bounds.height());
        root.addStyles(Style.BACKGROUND.is(Background.solid(Palette.TUSCANY)));
        root.add(new Label("Traveler's Notebook: Monster Tales").addStyles(Style.FONT.is(Typeface.PASSION_ONE.in(game).atSize(0.15f))));
        creationScreen = new PlayerCreationScreen(game);
        final Button startButton = new NavigationButton("Done", creationScreen);
        root.add(createNameArea());
        nameField.text.connect(new Slot<String>() {
            @Override
            public void onEmit(String s) {
                checkForCompletion();
            }
        });
        root.add(new Group(new FlowLayout())
                .add(startButton.setEnabled(false)));
    }

    private Group createNameArea() {
        Group group = new Group(AxisLayout.horizontal());
        group.add(new StyledLabel("Name:"));
        group.add(nameField = new Field()
                .setConstraint(Constraints.fixedSize(game.bounds.width() * 0.15f, game.bounds.height() * 0.08f)));
        group.addStyles(Style.BACKGROUND.is(Background.blank().inset(game.bounds.percentOfHeight(0.02f))));
        return group;
    }

    private void checkForCompletion() {
        final boolean isComplete = !nameField.text.get().trim().isEmpty();
        ((Value<Boolean>) complete).update(isComplete);
        creationScreen.setPlayers(createPlayerBuilders());
    }

    private Player.Builder[] createPlayerBuilders() {
        Player.Builder p1 = createPlayerBuilder().color(Palette.BLUE_LAGOON);
        Player.Builder p2 = createPlayerBuilder().color(Palette.TROPICAL_RAIN_FOREST);
        Player.Builder[] players = new Player.Builder[] {p1, p2};
        return players;
    }

    public Player.Builder createPlayerBuilder() {
        return new Player.Builder()
                .name(nameField.text.get().trim());
    }

    @Override
    public Game game() {
        return game;
    }
}
