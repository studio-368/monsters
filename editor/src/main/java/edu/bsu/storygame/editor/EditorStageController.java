package edu.bsu.storygame.editor;

import edu.bsu.storygame.editor.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeView;
import react.Slot;
import react.Value;

public class EditorStageController {

    @FXML
    MenuItem fileSaveMenuItem;
    @FXML
    private TreeView<String> narrativeTree;
    private final GsonParser parser = new GsonParser();
    private final Value<Narrative> narrative = Value.create(null);

    public EditorStageController() {
        configureNarrativeListener();
    }

    private void configureNarrativeListener() {
        narrative.connect(new Slot<Narrative>() {
            @Override
            public void onEmit(Narrative narrative) {
                updateNarrativeView();
            }
        });
        narrative.updateForce(null);
    }

    private void updateNarrativeView() {
        if (narrative.get() == null) return; // TODO: Make blank
        TreeItemWrapper<Narrative> root = new TreeItemWrapper<>("Narrative", narrative.get());
        addRegions(root);
        narrativeTree.setRoot(root);
    }

    private void addRegions(TreeItemWrapper<Narrative> root) {
        for (Region region : root.reference.regions) {
            TreeItemWrapper<Region> regionItem = new TreeItemWrapper<>(region.region, region);
            addEncounters(regionItem);
            root.getChildren().add(regionItem);
        }
    }

    private void addEncounters(TreeItemWrapper<Region> regions) {
        for (Encounter encounter : regions.reference.encounters) {
            TreeItemWrapper<Encounter> encounterItem = new TreeItemWrapper<>(encounter.name, encounter);
            addReactions(encounterItem);
            regions.getChildren().add(encounterItem);
        }
    }

    private void addReactions(TreeItemWrapper<Encounter> encounters) {
        for (Reaction reaction : encounters.reference.reactions) {
            TreeItemWrapper<Reaction> reactionItem = new TreeItemWrapper<>(reaction.name, reaction);
            addSkillTriggers(reactionItem);
            encounters.getChildren().add(reactionItem);
        }
    }

    private void addSkillTriggers(TreeItemWrapper<Reaction> reactions) {
        for (SkillTrigger trigger : reactions.reference.story.triggers) {
            TreeItemWrapper<SkillTrigger> item = new TreeItemWrapper<>(trigger.skill, trigger);
            reactions.getChildren().add(item);
        }
    }

    @FXML
    private void openFromText() {
        String jsonString = JsonPromptStage.prompt();
        if (jsonString == null) return;
        narrative.update(parser.parse(jsonString));
    }
}