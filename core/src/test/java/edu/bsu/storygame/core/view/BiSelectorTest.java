package edu.bsu.storygame.core.view;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import tripleplay.ui.ToggleButton;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BiSelectorTest {

    private BiSelector selector;
    private List<ToggleButton> buttons;

    @Before
    public void setUp() {
        this.selector = new BiSelector();
        this.buttons = Lists.newArrayList();
    }

    @Test
    public void testSelections_noSelections_sizeIsZero() {
        assertEquals(0, selector.selections().size());
    }

    @Test
    public void testSelect_sizeIsOne() {
        givenTrackedToggleButtons(1);
        whenTheFirstButtonIsToggled();
        thenTheNumberOfSelectionsIs(1);
    }

    private void thenTheNumberOfSelectionsIs(int number) {
        assertEquals(number, selector.selections().size());
    }

    private void whenTheFirstButtonIsToggled() {
        ToggleButton button = buttons.get(0);
        button.selected().update(!button.selected().get());
    }

    private void givenTrackedToggleButtons(int number) {
        for (; number > 0; number--) {
            ToggleButton button = new ToggleButton();
            buttons.add(button);
            selector.add(button);
        }
    }

    @Test
    public void testDeSelect_sizeIsZero() {
        givenTrackedToggleButtons(1);
        givenButtonsAreSelected(1);
        whenTheFirstButtonIsToggled();
        thenTheNumberOfSelectionsIs(0);
    }

    private void givenButtonsAreSelected(int number) {
        for (int i = 0; i < number; i++) {
            buttons.get(i).selected().update(true);
        }
    }

    @Test
    public void testDeselectOneAfterHavingTwoSelected_sizeIsOne() {
        givenTrackedToggleButtons(2);
        givenButtonsAreSelected(2);
        whenTheFirstButtonIsToggled();
        checkState(!buttons.get(0).selected().get());
        thenTheNumberOfSelectionsIs(1);
    }

    @Test
    public void testSelectElements_sizeIsTwo() {
        givenTrackedToggleButtons(2);
        whenButtonsAreSelected(2);
        assertEquals(2, selector.selections().size());
    }

    private void whenButtonsAreSelected(int number) {
        givenButtonsAreSelected(number);
    }

    @Test(expected = IllegalStateException.class)
    public void testThreeItemsAdded_twoSelected_thirdSelectionThrowsException() {
        givenTrackedToggleButtons(3);
        givenButtonsAreSelected(2);
        buttons.get(2).selected().update(true);
    }

    @Test
    public void testDisableButtonsAfterTwoSelections() {
        givenTrackedToggleButtons(3);
        whenButtonsAreSelected(2);
        assertFalse(buttons.get(2).isEnabled());
    }

}
