package edu.bsu.storygame.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScreamingCapitalizerTest {
    @Test
    public void testCapitalizer() {
        assertEquals("NORTHERN_EUROPE", ScreamingCapitalizer.convert("Northern Europe"));
    }
}
