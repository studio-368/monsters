/*
 * Copyright 2016 Paul Gestwicki
 *
 * This file is part of Traveler's Notebook: Monster Tales
 *
 * Traveler's Notebook: Monster Tales is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Traveler's Notebook: Monster Tales is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Traveler's Notebook: Monster Tales.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.bsu.storygame.core.view;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import tripleplay.ui.ToggleButton;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.*;

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
        assertEquals(0, selector.size());
    }

    @Test
    public void testSelect_sizeIsOne() {
        givenTrackedToggleButtons(1);
        whenTheFirstButtonIsToggled();
        thenTheNumberOfSelectionsIs(1);
    }

    private void givenTrackedToggleButtons(int number) {
        for (; number > 0; number--) {
            ToggleButton button = new ToggleButton();
            buttons.add(button);
            selector.add(button);
        }
    }

    private void whenTheFirstButtonIsToggled() {
        ToggleButton button = buttons.get(0);
        button.selected().update(!button.selected().get());
    }

    private void thenTheNumberOfSelectionsIs(int number) {
        assertEquals(number, selector.selections().size());
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
    public void testTwoSelections_otherButtonsAreDisabled() {
        givenTrackedToggleButtons(3);
        whenButtonsAreSelected(2);
        assertFalse(buttons.get(2).isEnabled());
    }

    @Test
    public void testTwoSelections_firstButtonIsStillEnabled() {
        givenTrackedToggleButtons(3);
        whenButtonsAreSelected(2);
        assertTrue(buttons.get(0).isEnabled());
    }

    @Test
    public void testTwoSelections_secondButtonIsStillEnabled() {
        givenTrackedToggleButtons(3);
        whenButtonsAreSelected(2);
        assertTrue(buttons.get(1).isEnabled());
    }

    @Test
    public void testTwoSelections_deselectOne_allButtonsEnabledAgain() {
        givenTrackedToggleButtons(3);
        givenButtonsAreSelected(2);
        whenButtonsAreDeselected(1);
        thenAllButtonsAreEnabled();
    }

    private void whenButtonsAreDeselected(int number) {
        for (int i = 0; i < number; i++) {
            buttons.get(i).selected().update(false);
        }
    }

    private void thenAllButtonsAreEnabled() {
        int disabled = 0;
        for (ToggleButton b : buttons) {
            if (!b.isEnabled()) {
                disabled++;
            }
        }
        assertEquals("Found " + disabled + " disabled buttons.", 0, disabled);
    }
}
