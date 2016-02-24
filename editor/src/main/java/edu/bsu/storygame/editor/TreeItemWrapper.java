package edu.bsu.storygame.editor;

import edu.bsu.storygame.editor.model.*;
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

    public static TreeItemWrapper<Narrative> wrap(Narrative narrative) {
        TreeItemWrapper<Narrative> root = new TreeItemWrapper<>("Narrative", narrative);
        addRegions(root);
        return root;
    }

    private static void addRegions(TreeItemWrapper<Narrative> root) {
        for (Region region : root.reference.regions) {
            TreeItemWrapper<Region> regionItem = new TreeItemWrapper<>(region.region, region);
            addEncounters(regionItem);
            root.getChildren().add(regionItem);
        }
    }

    private static void addEncounters(TreeItemWrapper<Region> regions) {
        for (Encounter encounter : regions.reference.encounters) {
            TreeItemWrapper<Encounter> encounterItem = new TreeItemWrapper<>(encounter.name, encounter);
            addReactions(encounterItem);
            regions.getChildren().add(encounterItem);
        }
    }

    private static void addReactions(TreeItemWrapper<Encounter> encounters) {
        for (Reaction reaction : encounters.reference.reactions) {
            TreeItemWrapper<Reaction> reactionItem = new TreeItemWrapper<>(reaction.name, reaction);
            addSkillTriggers(reactionItem);
            encounters.getChildren().add(reactionItem);
        }
    }

    private static void addSkillTriggers(TreeItemWrapper<Reaction> reactions) {
        for (SkillTrigger trigger : reactions.reference.story.triggers) {
            TreeItemWrapper<SkillTrigger> item = new TreeItemWrapper<>(trigger.skill, trigger);
            reactions.getChildren().add(item);
        }
    }
}
