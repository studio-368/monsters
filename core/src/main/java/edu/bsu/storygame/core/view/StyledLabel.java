package edu.bsu.storygame.core.view;

import tripleplay.ui.Label;

public class StyledLabel extends Label {

    public StyledLabel(String text) {
        super(text);
    }

    @Override
    protected Class<?> getStyleClass() {
        return StyledLabel.class;
    }
}
