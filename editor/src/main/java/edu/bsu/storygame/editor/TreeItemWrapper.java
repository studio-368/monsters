/*
 * Copyright 2016 Traveler's Notebook: Monster Tales project authors
 *
 * This file is part of monsters
 *
 * monsters is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * monsters is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with monsters.  If not, see <http://www.gnu.org/licenses/>.
 */

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
