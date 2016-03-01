package edu.bsu.storygame.core.util;

import com.google.common.collect.ImmutableMap;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import playn.core.Key;
import playn.core.Keyboard;
import react.SignalView;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class DebugKeys implements SignalView.Listener<Keyboard.Event> {

    private final ImmutableMap.Builder<Key, Runnable> builder = ImmutableMap.builder();

    {
        builder.put(Key.K1, new PointAdder(0))
                .put(Key.K2, new PointAdder(1));
    }

    private final ImmutableMap<Key, Runnable> actionMap = builder.build();
    private final GameContext context;

    public DebugKeys(GameContext context) {
        this.context = checkNotNull(context);
    }

    @Override
    public void onEmit(Keyboard.Event event) {
        if (event instanceof Keyboard.KeyEvent) {
            Keyboard.KeyEvent keyEvent = (Keyboard.KeyEvent) event;
            if (isActionTrigger(keyEvent)) {
                actionMap.get(keyEvent.key).run();
            }
        }
    }

    private boolean isActionTrigger(Keyboard.KeyEvent keyEvent) {
        return keyEvent.down
                && actionMap.containsKey(keyEvent.key);
    }

    private class PointAdder implements Runnable {
        private final int playerIndex;

        public PointAdder(int playerIndex) {
            checkArgument(playerIndex >= 0);
            this.playerIndex = playerIndex;
        }

        @Override
        public void run() {
            final Player player = context.players.get(playerIndex);
            final int points = player.storyPoints.get();
            player.storyPoints.update(points + 1);
        }
    }
}
