package edu.bsu.storygame.core.model;

import edu.bsu.storygame.core.MonsterGame;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public final class GameContextTest {

    @Test
    public void testEncounter_isClearedAtEndOfRound() {
        GameContext context = new GameContext(mock(MonsterGame.class),
                mock(Player.class),
                mock(Player.class));
        context.encounter.update(mock(Encounter.class));
        context.phase.update(Phase.END_OF_ROUND);
        assertNull(context.encounter.get());
    }
}
