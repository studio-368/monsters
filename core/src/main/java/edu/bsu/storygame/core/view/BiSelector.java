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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import react.RList;
import react.Slot;
import tripleplay.ui.ToggleButton;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public final class BiSelector {

    private static final int MAX_SELECTIONS = 2;
    public final RList<ToggleButton> selections = RList.create();
    private final List<ToggleButton> tracked = Lists.newArrayList();

    public List<ToggleButton> selections() {
        return ImmutableList.copyOf(selections);
    }

    public BiSelector() {
        configureButtonEnableManagementBasedOnSelectionSize();
    }

    private void configureButtonEnableManagementBasedOnSelectionSize() {
        selections.connect(new RList.Listener<ToggleButton>() {
            @Override
            public void onAdd(ToggleButton elem) {
                if (selections.size() == MAX_SELECTIONS) {
                    disableUnselectedItems();
                }
            }

            private void disableUnselectedItems() {
                for (ToggleButton button : tracked) {
                    button.setEnabled(button.selected().get());
                }
            }

            @Override
            public void onRemove(ToggleButton elem) {
                for (ToggleButton button : tracked) {
                    button.setEnabled(true);
                }
            }

            @Override
            public void onRemove(int index, ToggleButton elem) {
                this.onRemove(elem);
            }
        });
    }

    public void add(final ToggleButton button) {
        checkNotNull(button);
        tracked.add(button);
        button.selected().connect(new Slot<Boolean>() {
            @Override
            public void onEmit(Boolean isSelected) {
                if (isSelected) {
                    if (hasMaxSelections()) {
                        throw new IllegalStateException();
                    } else {
                        selections.add(button);
                    }
                } else {
                    boolean removed = selections.remove(button);
                    checkState(removed, "Attempted to remove an untracked button.");
                }
            }

            private boolean hasMaxSelections() {
                return selections.size() == 2;
            }
        });
    }

    public int size() {
        return selections.size();
    }
}
