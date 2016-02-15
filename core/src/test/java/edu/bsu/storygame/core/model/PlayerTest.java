package edu.bsu.storygame.core.model;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import tripleplay.util.Colors;

public class PlayerTest {

    @Test
    public void testEquals() {
        final String p1Name = "Player 1";
        final String p2Name = "Player 2";
        Player p1 = new Player(p1Name, Colors.BLUE);
        Player p2 = new Player(p2Name, Colors.BLUE);
        new EqualsTester()
                .addEqualityGroup(p1)
                .addEqualityGroup(p2)
                .testEquals();
    }
}
