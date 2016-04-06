package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.Typeface;
import playn.core.Game;
import react.SignalView;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.BorderLayout;
import tripleplay.ui.layout.FlowLayout;
import tripleplay.util.Colors;


public class PlayAgainScreen extends BoundedUIScreen {

    private final MonsterGame game;
    private final Stylesheet stylesheet;
    private Root root;

    public PlayAgainScreen(MonsterGame game) {
        super(game);
        this.game = game;
        this.stylesheet = GameStyle.newSheet(game);
        initRoot();
    }

    private void initRoot() {
        root = iface.createRoot(AxisLayout.vertical(), stylesheet, content)
                .setSize(content.width(), content.height())
                .add(createLayout());
    }

    private Group createLayout() {
        Group group = new Group(new BorderLayout())
                .addStyles(Style.BACKGROUND.is(Background.solid(Colors.BLACK)))
                .setConstraint(AxisLayout.stretched(1f));
        group.add(createContent().setConstraint(BorderLayout.CENTER));
        group.add(createFooter().setConstraint(BorderLayout.SOUTH));
        return group;
    }

    private Group createContent() {
        return new Group(new FlowLayout()).add(
                new Label("You both did some pretty awesome research!  Why not").addStyles(
                        Style.TEXT_WRAP.on,
                        Style.FONT.is(Typeface.OXYGEN.in(game).atSize(0.1f))
                ),
                new Button("Play Again").onClick(new SignalView.Listener<Button>() {
                    @Override
                    public void onEmit(Button event) {
                        game.screenStack.push(new StartScreen(game), game.screenStack.slide());
                    }
                }).addStyles(Style.FONT.is(Typeface.OXYGEN.in(game).atSize(0.1f))),
                new Label("and see what else you can find!").addStyles(
                        Style.TEXT_WRAP.on,
                        Style.FONT.is(Typeface.OXYGEN.in(game).atSize(0.1f))
                )
        );
    }

    private Group createFooter() {
        return new Group(AxisLayout.horizontal()).add(
                new Shim(0, 0).setConstraint(AxisLayout.stretched()),
                new Label("Thanks for playing!").addStyles(
                        Style.COLOR.is(Colors.WHITE)
                )
        );
    }

    @Override
    public Game game() {
        return game;
    }
}
