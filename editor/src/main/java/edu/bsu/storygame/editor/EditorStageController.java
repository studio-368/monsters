package edu.bsu.storygame.editor;

import edu.bsu.storygame.core.model.*;
import edu.bsu.storygame.editor.json.NarrativeParser;
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
    private Value<Narrative> narrative = Value.create(null);

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
        TreeItemMapping<String, Narrative> root = new TreeItemMapping<>("Narrative", narrative.get());
        addRegions(root);
        narrativeTree.setRoot(root);
    }

    private void addRegions(TreeItemMapping<String, Narrative> root) {
        for (Region region : Region.values()) {
            TreeItemMapping<String, Region> item = new TreeItemMapping<>(region.toString(), region);
            try {
                addEncounters(item);
            } catch (Exception e) {
                continue;
            }
            root.getChildren().add(item);
        }
    }

    private void addEncounters(TreeItemMapping<String, Region> toItem) {
        Region region = toItem.getMapping();
        for (Encounter encounter : narrative.get().forRegion(region)) {
            TreeItemMapping<String, Encounter> item = new TreeItemMapping<>(encounter.name, encounter);
            addReactions(item);
            toItem.getChildren().add(item);
        }
    }

    private void addReactions(TreeItemMapping<String, Encounter> toItem) {
        Encounter encounter = toItem.getMapping();
        for (Reaction reaction : encounter.reactions) {
            TreeItemMapping<String, Reaction> item = new TreeItemMapping<>(reaction.name, reaction);
            addSkillTriggers(item);
            toItem.getChildren().add(item);
        }
    }

    private void addSkillTriggers(TreeItemMapping<String, Reaction> toItem) {
        Reaction reaction = toItem.getMapping();
        for (SkillTrigger trigger : reaction.story.triggers) {
            TreeItemMapping<String, SkillTrigger> item = new TreeItemMapping<>(trigger.skill, trigger);
            toItem.getChildren().add(item);
        }
    }

    @FXML
    private void openFromText() {
        String jsonString = JsonPromptStage.prompt();
        if (jsonString == null) return;
        parse(jsonString);
    }

    private void parse(String jsonString) {
        NarrativeParser parser = new NarrativeParser();
        narrative.update(parser.parse(jsonString));
    }
}