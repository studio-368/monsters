package edu.bsu.storygame.core.view;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import react.Slot;
import tripleplay.ui.Togglable;
import tripleplay.ui.ToggleButton;

import java.util.List;

public class BiSelector {

    private List<Togglable<?>> selections = Lists.newArrayList();

    public List<Togglable<?>> selections() {
        return ImmutableList.copyOf(selections);
    }

    public void add(final Togglable<?> button) {
        if (isUpdatable()) {
            button.selected().connect(new Slot<Boolean>() {
                @Override
                public void onEmit(Boolean isSelected) {
                    if (isSelected) {
                        selections.add(button);
                    } else {
                        selections.remove(button);
                    }
                }
            });
        }
    }

    public boolean isUpdatable() {
        return (selections.size() < 2);
    }

    public void disableButtons() {
        for (Togglable<?> button : selections()) {
            ToggleButton toggled = (ToggleButton) button;
            toggled.setEnabled(false);
        }
    }
}
