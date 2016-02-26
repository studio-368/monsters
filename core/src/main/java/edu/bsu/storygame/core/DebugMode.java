package edu.bsu.storygame.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import edu.bsu.storygame.core.view.SampleGameScreen;
import playn.core.Key;
import playn.core.Keyboard;
import react.RList;
import react.SignalView;
import tripleplay.util.Colors;

import static com.google.common.base.Preconditions.checkNotNull;

public class DebugMode implements SignalView.Listener<Keyboard.Event> {

    private static final ImmutableList<String> SAMPLE_SKILLS = ImmutableList.of("WEAPON_USE", "MAGIC");

    private final ImmutableMap.Builder<Key, Runnable> builder = ImmutableMap.builder();

    {
        builder.put(Key.D, new Runnable() {
            @Override
            public void run() {
                GameContext context = new GameContext(game,
                        new Player.Builder().name("Ann").color(Colors.WHITE).skills(makeSkillList()).build(),
                        new Player.Builder().name("Barb").color(Colors.BLACK).skills(makeSkillList()).build());
                game.screenStack.push(new SampleGameScreen(game, context));
            }

            private RList<String> makeSkillList() {
                RList<String> list = RList.create();
                list.addAll(SAMPLE_SKILLS);
                return list;
            }
        });
    }

    private final MonsterGame game;
    private final ImmutableMap<Key, Runnable> actionMap = builder.build();

    public DebugMode(MonsterGame game) {
        this.game = checkNotNull(game);
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
                && keyEvent.isAltDown()
                && actionMap.containsKey(keyEvent.key);
    }
}
