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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReactionEditPane extends GridPane {
    private final EditorStageController parent;
    private final Reaction reaction;

    @FXML
    private Label reactionName;
    @FXML
    private TextField reactionNameTextField;
    @FXML
    private TextField reactionStoryTextField;
    @FXML
    private ListView<SkillTrigger> usableSkillsList;
    @FXML
    private ListView<SkillTrigger> availableSkillsList;
    @FXML
    private Button addSkillButton;
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
    private SkillTrigger selectedUsableSkill = null;
    private SkillTrigger selectedAvailableSkill = null;

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
        configureAvailableSkillsList();
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
        usableSkillsList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<SkillTrigger>) c -> {
            if (c.getList().size() == 0) {
                selectedUsableSkill = null;
                removeSkillButton.setDisable(true);
            } else {
                selectedUsableSkill = c.getList().get(0);
                removeSkillButton.setDisable(selectedUsableSkill.skill == null);
            }
            updateConclusionEditor();
        });
    }

    private void updateConclusionEditor() {
        if (selectedUsableSkill == null) {
            conclusionDescription.setText("No skill trigger selected");
            conclusionNarrativeTextArea.setText("");
            conclusionNarrativeTextArea.setDisable(true);
            resultsVBox.setDisable(true);
            pointsCheckBox.setSelected(false);
            pointsChangeTextField.clear();
            newSkillCheckBox.setSelected(false);
            newSkillTextField.clear();
        } else {
            conclusionDescription.setText(reaction.name + " with " + selectedUsableSkill.toString().toLowerCase());
            conclusionNarrativeTextArea.setText(selectedUsableSkill.conclusion.text);
            conclusionNarrativeTextArea.setDisable(false);
            resultsVBox.setDisable(false);
            pointsCheckBox.setSelected(selectedUsableSkill.conclusion.points != null);
            pointsChangeTextField.setDisable(false);
            if (selectedUsableSkill.conclusion.points != null)
                pointsChangeTextField.setText("" + selectedUsableSkill.conclusion.points);
            else
                pointsChangeTextField.clear();
            newSkillCheckBox.setSelected(selectedUsableSkill.conclusion.skill != null);
            newSkillTextField.setText(selectedUsableSkill.conclusion.skill);
        }
    }

    private void configureAvailableSkillsList() {
        availableSkillsList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<SkillTrigger>) c -> {
            if (c.getList().size() == 0) {
                selectedAvailableSkill = null;
                addSkillButton.setDisable(true);
            } else {
                selectedAvailableSkill = c.getList().get(0);
                addSkillButton.setDisable(false);
            }
        });
    }

    private void populateReaction() {
        reactionName.setText(reaction.name + " reaction");
        reactionNameTextField.setText(reaction.name);
        usableSkillsList.setItems(new ObservableListWrapper<>(reaction.story.triggers));
        refreshAvailableSkills();
    }

    private void refreshAvailableSkills() {
        List<String> skills = new ArrayList<>();
        usableSkillsList.getItems().forEach(skillTrigger -> skills.add(skillTrigger.skill));
        availableSkillsList.getItems().forEach(skillTrigger -> skills.add(skillTrigger.skill));
        for (String skill : SkillTrigger.availableSkills) {
            if (!skills.contains(skill))
                availableSkillsList.getItems().add(new SkillTrigger(skill, Conclusion.emptyConclusion()));
        }
    }

    @FXML
    private void onAddSkill() {
        usableSkillsList.getItems().add(selectedAvailableSkill);
        usableSkillsList.getSelectionModel().select(selectedAvailableSkill);
        availableSkillsList.getItems().remove(selectedAvailableSkill);
        parent.refresh();
    }

    @FXML
    private void onRemoveSkill() {
        availableSkillsList.getItems().add(selectedUsableSkill);
        availableSkillsList.getSelectionModel().select(selectedUsableSkill);
        usableSkillsList.getItems().remove(selectedUsableSkill);
        parent.refresh();
    }

    @FXML
    private void onReactionNameChange() {
        reaction.name = reactionNameTextField.getText();
        reactionName.setText(reaction.name);
    }

    @FXML
    private void onStoryChange() {
        reaction.story.text = reactionStoryTextField.getText();
    }

    @FXML
    private void onNarrativeChange() {
        selectedUsableSkill.conclusion.text = conclusionNarrativeTextArea.getText();
    }

    @FXML
    private void onStoryPointsChange() {
        if (!pointsCheckBox.isSelected()) {
            selectedUsableSkill.conclusion.points = null;
        } else {
            updateStoryPoints();
        }
    }

    @FXML
    private void onNewSkillChange() {
        if (!newSkillCheckBox.isSelected()) {
            selectedUsableSkill.conclusion.skill = null;
        } else {
            selectedUsableSkill.conclusion.skill = newSkillTextField.getText();
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
        selectedUsableSkill.conclusion.points = points;
        pointsChangeTextField.setStyle("");

    }


}