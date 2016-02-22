package edu.bsu.storygame.core.view;

import org.junit.Before;
import org.junit.Test;
import tripleplay.ui.ToggleButton;

import static org.junit.Assert.assertEquals;

public class BiSelectorTest {

    private BiSelector selector;
    private ToggleButton button;

    @Before
    public void setUp() {
        this.selector = new BiSelector();
    }

    @Test
    public void testSelections_noSelections_sizeIsZero() {
        assertEquals(0, selector.selections().size());
    }

    @Test
    public void testSelect_sizeIsOne() {
        givenATrackedToggleButton();
        button.selected().update(true);
        assertEquals(1, selector.selections().size());
    }

    private void givenATrackedToggleButton() {
        button = new ToggleButton();
        selector.add(button);
    }

    @Test
    public void testDeSelect_sizeIsZero() {
        givenATrackedToggleButton();
        button.selected().update(true);
        button.selected().update(false);
        assertEquals(0, selector.selections().size());
    }
}
