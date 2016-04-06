package edu.bsu.storygame.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import edu.bsu.storygame.core.model.GameContext;
import edu.bsu.storygame.core.model.Player;
import edu.bsu.storygame.core.model.Region;
import edu.bsu.storygame.core.model.Skill;
import edu.bsu.storygame.core.view.GameScreen;
import edu.bsu.storygame.core.view.Palette;
import playn.core.Key;
import playn.core.Keyboard;
import react.SignalView;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class KeystrokeBasedPlayerGenerator implements SignalView.Listener<Keyboard.Event> {

    private static final ImmutableList<Skill> SAMPLE_SKILLS = ImmutableList.of(
            Skill.named("Weapon use"),
            Skill.named("Magic")
    );

    private final ImmutableMap.Builder<Key, Runnable> builder = ImmutableMap.builder();

    {
        builder.put(Key.D, new Runnable() {
            @Override
            public void run() {
                GameContext context = new GameContext(game,
                        new Player.Builder().name("Bonnie")
                                .color(Palette.PLAYER_ONE)
                                .location(Region.AFRICA)
                                .skills(makeSkillList())
                                .build(),
                        new Player.Builder()
                                .name("Clyde")
                                .color(Palette.PLAYER_TWO)
                                .location(Region.NORTH_AMERICA)
                                .skills(makeSkillList())
                                .build());
                GameScreen gameScreen = new GameScreen(context);
                game.screenStack.push(gameScreen);
            }

            private List<Skill> makeSkillList() {
                List<Skill> list = Lists.newArrayList();
                list.addAll(SAMPLE_SKILLS);
                return list;
            }
        });
    }

    private final MonsterGame game;
    private final ImmutableMap<Key, Runnable> actionMap = builder.build();

    public KeystrokeBasedPlayerGenerator(MonsterGame game) {
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
