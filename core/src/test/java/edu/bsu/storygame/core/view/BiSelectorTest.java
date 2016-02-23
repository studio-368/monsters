package edu.bsu.storygame.core.view;

import org.junit.Before;
import org.junit.Test;
import tripleplay.ui.ToggleButton;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void testThreeItemsAdded_TwoSelected() {
        givenTwoTrackedToggleButtons();
        ToggleButton thirdButton = new ToggleButton();
        button.selected().update(selector.isUpdatable());
        secondButton.selected().update(selector.isUpdatable());
        selector.add(thirdButton);
        thirdButton.selected().update(selector.isUpdatable());
        assertEquals(2, selector.selections().size());
    }

    @Test
    public void disableButtons() {
        givenTwoTrackedToggleButtons();
        ToggleButton thirdButton = new ToggleButton();
        button.selected().update(selector.isUpdatable());
        secondButton.selected().update(selector.isUpdatable());
        selector.add(thirdButton);
        thirdButton.selected().update(selector.isUpdatable());
        if (!selector.isUpdatable()) {
            selector.disableButtons();
        }
        ToggleButton toggled = (ToggleButton) selector.selections().get(0);
        assertEquals(false, toggled.isEnabled());
    }

}
