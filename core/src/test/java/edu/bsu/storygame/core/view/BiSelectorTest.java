package edu.bsu.storygame.core.view;

import org.junit.Before;
import org.junit.Test;
import tripleplay.ui.ToggleButton;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BiSelectorTest {

    private BiSelector selector;
    private ToggleButton button;
    private ToggleButton secondButton;

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

    private void givenTwoTrackedToggleButtons() {
        button = new ToggleButton();
        selector.add(button);
        secondButton = new ToggleButton();
        selector.add(secondButton);
    }

    @Test
    public void testDeSelect_sizeIsZero() {
        givenATrackedToggleButton();
        button.selected().update(true);
        button.selected().update(false);
        assertEquals(0, selector.selections().size());
    }

    @Test
    public void testDeSelect_sizeIsOne() {
        givenTwoTrackedToggleButtons();
        button.selected().update(true);
        secondButton.selected().update(true);
        button.selected().update(false);
        assertEquals(1, selector.selections().size());
    }

    @Test
    public void testSelectElements_sizeIsTwo() {
        givenTwoTrackedToggleButtons();
        button.selected().update(true);
        secondButton.selected().update(true);
        assertEquals(2, selector.selections().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testThreeItemsAdded_twoSelected_thirdSelectionThrowsException() {
        givenTwoTrackedToggleButtons();
        ToggleButton thirdButton = new ToggleButton();
        selector.add(thirdButton);
        button.selected().update(true);
        secondButton.selected().update(true);
        thirdButton.selected().update(true);
    }

    @Test
    public void testDisableButtonsAfterTwoSelections() {
        givenTwoTrackedToggleButtons();
        ToggleButton thirdButton = new ToggleButton();
        selector.add(thirdButton);
        button.selected().update(true);
        secondButton.selected().update(true);
        assertFalse(thirdButton.isEnabled());
    }

}
