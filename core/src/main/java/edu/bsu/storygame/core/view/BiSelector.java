package edu.bsu.storygame.core.view;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import react.RList;
import react.Slot;
import tripleplay.ui.ToggleButton;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class BiSelector {

    private static final int MAX_SELECTIONS = 2;
    private final RList<ToggleButton> selections = RList.create();
    private final List<ToggleButton> tracked = Lists.newArrayList();

    public List<ToggleButton> selections() {
        return ImmutableList.copyOf(selections);
    }


    public BiSelector() {
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
                    selections.remove(button);
                }
            }

            private boolean hasMaxSelections() {
                return selections.size() == 2;
            }
        });
    }
}
