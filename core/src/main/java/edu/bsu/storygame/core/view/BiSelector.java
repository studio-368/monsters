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
