package edu.bsu.storygame.core.view;

import tripleplay.ui.Field;
import tripleplay.ui.Group;

public abstract class PlayerCreationGroup {

    public abstract Group createPlayerGroup(Field nameField, Group skillGroup);

    public abstract Group createSkillGroup();

}
