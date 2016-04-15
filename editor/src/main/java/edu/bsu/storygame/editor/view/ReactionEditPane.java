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

package edu.bsu.storygame.editor.view;

import com.sun.javafx.collections.ObservableListWrapper;
import edu.bsu.storygame.editor.EditorStageController;
import edu.bsu.storygame.editor.model.Conclusion;
import edu.bsu.storygame.editor.model.Reaction;
import edu.bsu.storygame.editor.model.SkillTrigger;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ReactionEditPane extends EditPane {
    private final EditorStageController parent;
    private final Reaction reaction;

    @FXML
    private Label reactionName;
    @FXML
    private TextField reactionNameTextField;
    @FXML
    private TextField reactionStoryTextField;
    @FXML
    private ListView<SkillTrigger> skillsList;
    @FXML
    private Button addSkillButton;
    @FXML
    private Button renameSkillButton;
    @FXML
    private Button removeSkillButton;
    @FXML
    private Label conclusionDescription;
    @FXML
    private TextArea conclusionNarrativeTextArea;
    @FXML
    private VBox resultsVBox;
    @FXML
    private CheckBox pointsCheckBox;
    @FXML
    private TextField pointsChangeTextField;
    @FXML
    private CheckBox newSkillCheckBox;
    @FXML
    private TextField newSkillTextField;
    private SkillTrigger selectedSkill = null;

    public ReactionEditPane(Reaction reaction, EditorStageController parent) {
        this.parent = parent;
        this.reaction = reaction;
        loadFxml();
        configure();
        populateReaction();
    }

    private void loadFxml() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/ReactionEdit.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configure() {
        pointsChangeTextField.setText("1");
        configureUsableSkillsList();
        bindTextField(reactionNameTextField, (o, v, n) -> onReactionNameChange());
        bindTextField(reactionStoryTextField, (o, v, n) -> onStoryChange());
        bindTextArea(conclusionNarrativeTextArea, (o, v, n) -> onNarrativeChange());
        bindTextField(pointsChangeTextField, (o, v, n) -> onStoryPointsChange());
        bindTextField(newSkillTextField, (o, v, n) -> onNewSkillChange());
    }

    private void bindTextField(TextField field, ChangeListener<String> listener) {
        field.textProperty().addListener(listener);
    }

    private void bindTextArea(TextArea field, ChangeListener<String> listener) {
        field.textProperty().addListener(listener);
    }

    private void configureUsableSkillsList() {
        skillsList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<SkillTrigger>) c -> {
            if (c.getList().size() == 0) {
                selectedSkill = null;
                renameSkillButton.setDisable(true);
                removeSkillButton.setDisable(true);
            } else {
                selectedSkill = c.getList().get(0);
                renameSkillButton.setDisable(selectedSkill.skill == null);
                removeSkillButton.setDisable(selectedSkill.skill == null);
            }
            updateConclusionEditor();
        });
    }

    private void updateConclusionEditor() {
        if (selectedSkill == null) {
            conclusionDescription.setText("No skill trigger selected");
            conclusionNarrativeTextArea.setText("");
            conclusionNarrativeTextArea.setDisable(true);
            resultsVBox.setDisable(true);
            pointsCheckBox.setSelected(false);
            pointsChangeTextField.clear();
            newSkillCheckBox.setSelected(false);
            newSkillTextField.clear();
        } else {
            conclusionDescription.setText(reaction.name + " with " + selectedSkill.toString().toLowerCase());
            conclusionNarrativeTextArea.setText(selectedSkill.conclusion.text);
            conclusionNarrativeTextArea.setDisable(false);
            resultsVBox.setDisable(false);
            pointsCheckBox.setSelected(selectedSkill.conclusion.points != null);
            pointsChangeTextField.setDisable(false);
            if (selectedSkill.conclusion.points != null)
                pointsChangeTextField.setText("" + selectedSkill.conclusion.points);
            else
                pointsChangeTextField.clear();
            newSkillCheckBox.setSelected(selectedSkill.conclusion.skill != null);
            newSkillTextField.setText(selectedSkill.conclusion.skill);
        }
    }

    private void populateReaction() {
        skillsList.setItems(new ObservableListWrapper<>(reaction.story.triggers));
        refresh();
    }

    @FXML
    private void onAddSkill() {
        String newSkillName = TextPrompt.emptyPrompt();
        SkillTrigger trigger;
        if (newSkillName.equals("") || newSkillName.equalsIgnoreCase("No skill")) {
            trigger = new SkillTrigger(null, Conclusion.emptyConclusion());
        } else {
            trigger = new SkillTrigger(newSkillName, Conclusion.emptyConclusion());
        }
        skillsList.getItems().add(trigger);
        skillsList.getSelectionModel().select(trigger);
    }

    @FXML
    private void onRemoveSkill() {
        skillsList.getItems().remove(selectedSkill);
    }

    @FXML
    private void onRenameSkill() {
        selectedSkill.skill = TextPrompt.promptFromString(selectedSkill.skill);
        skillsList.getProperties().put("listRecreateKey", Boolean.TRUE);
    }

    @FXML
    private void onReactionNameChange() {
        reaction.name = reactionNameTextField.getText();
        reactionName.setText(reaction.name);
        parent.refresh();
    }

    @FXML
    private void onStoryChange() {
        reaction.story.text = reactionStoryTextField.getText();
    }

    @FXML
    private void onNarrativeChange() {
        selectedSkill.conclusion.text = conclusionNarrativeTextArea.getText();
    }

    @FXML
    private void onStoryPointsChange() {
        if (selectedSkill == null) return;
        if (!pointsCheckBox.isSelected()) {
            selectedSkill.conclusion.points = null;
        } else {
            updateStoryPoints();
        }
    }

    @FXML
    private void onNewSkillChange() {
        if (!newSkillCheckBox.isSelected()) {
            selectedSkill.conclusion.skill = null;
        } else {
            selectedSkill.conclusion.skill = newSkillTextField.getText();
        }
    }

    private void updateStoryPoints() {
        int points;
        try {
            points = Integer.valueOf(pointsChangeTextField.getText());
        } catch (Exception e) {
            pointsChangeTextField.setStyle("-fx-text-fill: #FF0000");
            return;
        }
        selectedSkill.conclusion.points = points;
        pointsChangeTextField.setStyle("");

    }

    public void refresh() {
        reactionName.setText(reaction.name + " reaction");
        reactionNameTextField.setText(reaction.name);
        reactionStoryTextField.setText(reaction.story.text);
        skillsList.getProperties().put("listRecreateKey", Boolean.TRUE);
        updateConclusionEditor();
    }


}
