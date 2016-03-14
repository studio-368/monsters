package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import edu.bsu.storygame.core.assets.TileCache;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Phase;
import edu.bsu.storygame.core.util.DebugKeys;
import playn.core.Game;
import playn.scene.GroupLayer;
import playn.scene.Layer;
import react.Slot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.Background;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AbsoluteLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class SampleGameScreen extends ScreenStack.UIScreen {

    private final MonsterGame game;
    private final GameContext context;
    private final GroupLayer boundedLayer;

    public SampleGameScreen(final MonsterGame game, final GameContext context) {
        super(checkNotNull(game).plat);
        this.game = game;
        this.context = context;
        this.boundedLayer = new GroupLayer(game.bounds.width(), game.bounds.height());
        layer.addAt(boundedLayer,
                (game.plat.graphics().viewSize.width() - game.bounds.width()) / 2,
                (game.plat.graphics().viewSize.height() - game.bounds.height()) / 2);

        configurePlayerAdvancementAtEndOfRound();

        if (game.config.debugMode()) {
            game.plat.input().keyboardEvents.connect(new DebugKeys(context));
        }
    }

    @Override
    public void wasAdded() {
        super.wasAdded();
        createUI();
    }

    // We are using some extra variables to enhance readability.
    @SuppressWarnings("UnnecessaryLocalVariable")
    private void createUI() {
        final float sidebarX = 0;
        final float sidebarY = 0;
        final float sidebarWidth = game.bounds.width() * 0.25f;
        final float sidebarHeight = game.bounds.height();
        final float mapX = game.bounds.width() * 0.25f;
        final float mapY = 0;
        final float mapWidth = game.bounds.width() * 0.75f;
        final float mapHeight = game.bounds.height();

        iface.createRoot(new AbsoluteLayout(), GameStyle.newSheet(game), boundedLayer)
                .setSize(boundedLayer.width(), boundedLayer.height())
                .setStyles(Style.BACKGROUND.is(Background.image(game.tileCache.tile(TileCache.Key.BACKGROUND))))
                .add(AbsoluteLayout.at(new Sidebar(context), sidebarX, sidebarY, sidebarWidth, sidebarHeight))
                .add(AbsoluteLayout.at(new MapView(context), mapX, mapY, mapWidth, mapHeight));

        final float cardWidth = mapWidth * 0.68f;
        final float cardHeight = mapHeight;
        final float cardX = mapX + (mapWidth - cardWidth) / 2f;
        final float cardY = 0;
        final EncounterCardFactory factory = new EncounterCardFactory(context);
        final Layer encounterCard = factory.create(cardWidth, cardHeight, iface);
        encounterCard.setVisible(false);
        boundedLayer.addAt(encounterCard, cardX, cardY);
        context.phase.connect(new Slot<Phase>() {
            final float duration = 300f;

            @Override
            public void onEmit(Phase phase) {
                if (phase == Phase.ENCOUNTER) {
                    animateCardEntrance();
                } else if (phase == Phase.END_OF_ROUND) {
                    animateCardRemoval();
                }
            }

            private void animateCardEntrance() {
                encounterCard.setVisible(true);
                iface.anim.tweenTranslation(encounterCard)
                        .from(game.plat.graphics().viewSize.width(), cardY + cardHeight / 2f)
                        .to(cardX, cardY)
                        .in(duration)
                        .easeIn();
            }

            private void animateCardRemoval() {
                iface.anim.tweenTranslation(encounterCard)
                        .to(game.plat.graphics().viewSize.width(), cardY - cardHeight / 2f)
                        .in(duration)
                        .easeOut()
                        .then()
                        .action(new Runnable() {
                            @Override
                            public void run() {
                                encounterCard.setVisible(false);
                            }
                        });
            }
        });

        Layer handoffDialog = new HandoffDialogFactory(context).create(iface);
        boundedLayer.addAt(handoffDialog, (boundedLayer.width() - handoffDialog.width()) / 2, (boundedLayer.height() - handoffDialog.height()) / 2);
    }


    private void configurePlayerAdvancementAtEndOfRound() {
        context.phase.connect(new Slot<Phase>() {
            @Override
            public void onEmit(Phase phase) {
                if (phase.equals(Phase.END_OF_ROUND)) {
                    if (currentPlayerHasEnoughPointsToWin()) {
                        context.currentPlayer.get().hasWon.update(true);
                        configureWinScreen();
                    } else {
                        advancePlayer();
                        context.phase.update(Phase.MOVEMENT);
                    }
                }
            }

            private boolean currentPlayerHasEnoughPointsToWin() {
                return context.currentPlayer.get().storyPoints.get() >= context.pointsRequiredForVictory;
            }

            private void configureWinScreen() {
                game.screenStack.push(new WinScreen(context, context.currentPlayer.get()), game.screenStack.slide());
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
