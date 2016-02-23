package edu.bsu.storygame.editor;


import javafx.scene.Node;
import javafx.scene.control.TreeItem;

public class TreeItemMapping<K, V> extends TreeItem<K> {

    private final V mapping;

    public TreeItemMapping(K item, V mapping) {
        this(item, mapping, null);
    }

    public TreeItemMapping(K item, V mapping, Node graphic) {
        super(item, graphic);
        this.mapping = mapping;
    }

    public V getMapping() {
        return mapping;
    }

}
