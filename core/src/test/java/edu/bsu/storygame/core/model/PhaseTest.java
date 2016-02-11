package edu.bsu.storygame.core.model;

import edu.bsu.storygame.core.model.Phase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PhaseTest {

    @Parameterized.Parameter
    public Phase initial;

    @Parameterized.Parameter(value = 1)
    public Phase next;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { Phase.MOVEMENT, Phase.ENCOUNTER },
                { Phase.ENCOUNTER, Phase.STORY },
                { Phase.STORY, Phase.END_OF_ROUND }
        });
    }

    @Test
    public void testNext() {
        assertEquals(next, initial.next());
    }

}
