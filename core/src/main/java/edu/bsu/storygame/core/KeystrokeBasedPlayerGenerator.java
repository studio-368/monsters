/*
 * Copyright 2016 Paul Gestwicki
 *
 * This file is part of Traveler's Notebook: Monster Tales
 *
 * Traveler's Notebook: Monster Tales is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Traveler's Notebook: Monster Tales is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Traveler's Notebook: Monster Tales.  If not, see <http://www.gnu.org/licenses/>.
 */

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
