package edu.bsu.storygame.editor.view;

import com.sun.javafx.collections.ObservableListWrapper;
import edu.bsu.storygame.editor.EditorStageController;
import edu.bsu.storygame.editor.model.Conclusion;
import edu.bsu.storygame.editor.model.Reaction;
import edu.bsu.storygame.editor.model.SkillTrigger;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

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
    private CheckBox pointsCheckBox;
    @FXML
    private TextField pointsChangeTextField;
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

    private void initialize() {

    }

    private void configure() {
        pointsChangeTextField.setText("1");
        configureUsableSkillsList();
        configureAvailableSkillsList();
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
            pointsCheckBox.setSelected(false);
            pointsCheckBox.setDisable(true);
        } else {
            conclusionDescription.setText(reaction.name + " with " + selectedUsableSkill.toString().toLowerCase());
            conclusionNarrativeTextArea.setText(selectedUsableSkill.conclusion.text);
            conclusionNarrativeTextArea.setDisable(false);
            pointsCheckBox.setSelected(selectedUsableSkill.conclusion.points != null);
            pointsCheckBox.setDisable(false);
            pointsChangeTextField.setDisable(false);
            pointsChangeTextField.setText("" + selectedUsableSkill.conclusion.points);
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


}
