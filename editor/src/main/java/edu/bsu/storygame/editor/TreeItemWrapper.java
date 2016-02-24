package edu.bsu.storygame.editor;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeItem;

public class TreeItemWrapper<T> extends TreeItem<String> {

    public final T reference;
    public final SimpleStringProperty name;

    public TreeItemWrapper(String name, T reference) {
        super(name);
        this.reference = reference;
        this.name = new SimpleStringProperty(name);
    }

    @Override
    public String toString() {
        return name.getValue();
    }
}
