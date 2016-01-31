package edu.bsu.storygame.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class PhaseTest {
    @Test
    public void testNext() {
        Phase p = Phase.values()[0];
        assertEquals(Phase.values()[1], p.next());
    }

    @Test
    public void testNext_nextOfLast() {
        Phase p = Phase.values()[Phase.values().length-1];
        assertEquals(Phase.values()[0], p.next());
    }
}
