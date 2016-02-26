package edu.bsu.storygame.core.model;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import react.RList;
import tripleplay.util.Colors;

import java.util.ArrayList;

public class PlayerTest {

    @Test
    public void testEquals() {
        final String p1Name = "Player 1";
        final String p2Name = "Player 2";
        final RList<String> skills = new RList<>(new ArrayList<String>());
        Player p1 = new Player.Builder().name(p1Name)
                .color(Colors.CYAN).skills(skills).build();
        Player p2 = new Player.Builder().name(p2Name)
                .color(Colors.CYAN).skills(skills).build();
        new EqualsTester()
                .addEqualityGroup(p1)
                .addEqualityGroup(p2)
                .testEquals();
    }
}
