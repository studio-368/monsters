package edu.bsu.storygame.core.model;

import com.google.common.collect.Lists;
import edu.bsu.storygame.core.MonsterGame;
import org.junit.Test;
import tripleplay.util.Colors;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public final class GameContextTest {

    @Test
    public void testEncounter_isClearedAtEndOfRound() {
        final List<Skill> skills = Lists.newArrayList();
        GameContext context = new GameContext(mock(MonsterGame.class),
                new Player.Builder().name("Jack")
                        .color(Colors.CYAN).skills(skills).build(),
                new Player.Builder().name("Also Jack")
                        .color(Colors.CYAN).skills(skills).build());
        context.encounter.update(mock(Encounter.class));
        context.phase.update(Phase.END_OF_ROUND);
        assertNull(context.encounter.get());
    }
}
