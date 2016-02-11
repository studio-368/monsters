package edu.bsu.storygame.core;

import com.google.common.testing.EqualsTester;
import edu.bsu.storygame.core.model.Player;
import org.junit.Test;

public class PlayerTest {

    @Test
    public void testEquals() {
        final String p1Name = "Player 1";
        final String p2Name = "Player 2";
        Player p1 = new Player(p1Name);
        Player p2 = new Player(p2Name);
        new EqualsTester()
                .addEqualityGroup(p1)
                .addEqualityGroup(p2)
                .testEquals();
    }
}
