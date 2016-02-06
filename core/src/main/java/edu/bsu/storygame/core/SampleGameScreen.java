package edu.bsu.storygame.core;

import playn.core.Game;
import playn.scene.Mouse;
import playn.scene.Pointer;
import react.Connection;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;

import static com.google.common.base.Preconditions.checkNotNull;

public class SampleGameScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;
    private final GameContext context;

    public SampleGameScreen(final MonsterGame game) {
        super(checkNotNull(game).plat);
        this.game = game;
        this.context = new GameContext(game, new Player("Abigail"), new Player("Bruce"));
        configurePointerInput();
        createUI();
        context.phase.connect(new Slot<Phase>() {
            private Connection connection;

            @Override
            public void onEmit(Phase phase) {
                if (phase.equals(Phase.ENCOUNTER)) {
                    popupEncounterDialog();
                }
            }

            private void popupEncounterDialog() {
                final Root dialog = iface.createRoot(AxisLayout.vertical(), SimpleStyles.newSheet(game.plat.graphics()), layer);
                dialog.setStyles(Style.BACKGROUND.is(Background.solid(Colors.LIGHT_GRAY)));
                dialog.setSize(size().width() * 0.8f, size().height() * 0.8f)
                        .setLocation(size().width() * 0.1f, size().height());
                iface.anim.tweenY(dialog.layer)
                        .to(size().height() * 0.1f)
                        .in(200f)
                        .easeIn();
                dialog.add(new EncounterView(context, new Encounter(context)));

                connection = context.phase.connect(new Slot<Phase>() {
                    @Override
                    public void onEmit(Phase phase) {
                        if (phase.equals(Phase.END_OF_ROUND)) {
                            iface.removeRoot(dialog);
                            connection.close();
                        }
                    }
                });
            }
        });
        configurePlayerAdvancementAtEndOfRound();
    }

    private void configurePointerInput() {
        new Pointer(game().plat, layer, true);
        game().plat.input().mouseEvents.connect(new Mouse.Dispatcher(layer, true));
    }

    private void createUI() {
        iface.createRoot(AxisLayout.vertical().offStretch(), SimpleStyles.newSheet(game.plat.graphics()), layer)
                .setSize(size())
                .setStyles(Style.BACKGROUND.is(Background.image(game.plat.assets().getImage("images/bg.png"))))
                .add(new Label() {
                    {
                        updateText();
                        context.currentPlayer.connect(new Slot<Player>() {
                            @Override
                            public void onEmit(Player player) {
                                updateText();
                            }

                        });
                    }

                    private void updateText() {
                        text.update(context.currentPlayer.get().name + "\'s turn");
                    }
                })
                .add(new Label() {
                         {
                             updateText();
                             SampleGameScreen.this.context.phase.connect(new Slot<Phase>() {
                                 @Override
                                 public void onEmit(Phase phase) {
                                     updateText();
                                 }
                             });
                         }

                         private void updateText() {
                             text.update("Current phase: " + context.phase.get().name());
                         }
                     },
                        new MapView(context));
    }

    private void configurePlayerAdvancementAtEndOfRound() {
        context.phase.connect(new Slot<Phase>() {
            @Override
            public void onEmit(Phase phase) {
                if (phase.equals(Phase.END_OF_ROUND)) {
                    advancePlayer();
                    context.phase.update(Phase.MOVEMENT);
                }
            }

            private void advancePlayer() {
                for (int i = 0, limit = context.players.size(); i < limit; i++) {
                    if (context.currentPlayer.get().equals(context.players.get(i))) {
                        context.currentPlayer.update(context.players.get((i + 1) % context.players.size()));
                        return;
                    }
                }
                throw new IllegalStateException("Cannot advance player");
            }
        });
    }


    @Override
    public Game game() {
        return game;
    }
}
