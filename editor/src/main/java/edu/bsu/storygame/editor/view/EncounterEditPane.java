package edu.bsu.storygame.editor.view;

import com.sun.javafx.collections.ObservableListWrapper;
import edu.bsu.storygame.editor.EditorStageController;
import edu.bsu.storygame.editor.model.Encounter;
import edu.bsu.storygame.editor.model.Reaction;
import edu.bsu.storygame.editor.model.Story;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Optional;

public class EncounterEditPane extends GridPane {

    private final Encounter encounter;
    private final EditorStageController parent;

    @FXML
    private ListView<Reaction> encounterReactionsList;
    @FXML
    private Label encounterName;
    @FXML
    private Button reactionAddButton;
    @FXML
    private Button reactionUpButton;
    @FXML
    private Button reactionDownButton;
    @FXML
    private Button reactionRenameButton;
    @FXML
    private TextField encounterNameTextField;
    @FXML
    private TextField encounterImage;
    @FXML
    private Button reactionDeleteButton;
    private Reaction selectedReaction = null;

    public EncounterEditPane(Encounter encounter, EditorStageController parent) {
        this.parent = parent;
        this.encounter = encounter;
        loadFxml();
        configure();
        populate();
    }

    private void loadFxml() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/EncounterEdit.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configure() {
        encounterReactionsList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Reaction>) c -> {
            if (c.getList().size() == 0) {
                selectedReaction = null;
                setReactionButtonsDisabled(true);
            } else {
                selectedReaction = c.getList().get(0);
                setReactionButtonsDisabled(false);
            }
        });
    }

    private void setReactionButtonsDisabled(boolean disabled) {
        ObservableList<Reaction> items = encounterReactionsList.getItems();
        boolean topOfList = items.indexOf(selectedReaction) == 0;
        boolean bottomOfList = items.indexOf(selectedReaction) == items.size() - 1;
        reactionRenameButton.setDisable(disabled);
        reactionDeleteButton.setDisable(disabled);
        reactionUpButton.setDisable(disabled || topOfList);
        reactionDownButton.setDisable(disabled || bottomOfList);
    }

    private void populate() {
        encounterName.setText(encounter.name + " encounter");
        encounterNameTextField.setText(encounter.name);
        encounterReactionsList.setItems(new ObservableListWrapper<>(encounter.reactions));
        encounterImage.setText(encounter.image);
    }

    @FXML
    private void onReactionAdd() {
        String reactionName = TextPrompt.emptyPrompt();
        if (reactionName == null) return;
        Reaction reaction = new Reaction(reactionName, Story.emptyStory());
        encounterReactionsList.getItems().add(reaction);
        refresh();
    }

    @FXML
    private void onReactionDelete() {
        if (confirm("Are you sure you want to delete this?")) {
            encounterReactionsList.getItems().remove(selectedReaction);
            refresh();
        }
    }


    @FXML
    private void onReactionRename() {
        selectedReaction.name = TextPrompt.promptFromString(selectedReaction.name);
        refresh();
    }

    @FXML
    private void onReactionUpwards() {
        ObservableList<Reaction> list = encounterReactionsList.getItems();
        int index = list.indexOf(selectedReaction);
        Reaction oldReaction = list.remove(index);
        list.add(index - 1, oldReaction);
        refresh();
        encounterReactionsList.getSelectionModel().selectIndices(index - 1);
    }

    @FXML
    private void onReactionDownwards() {
        ObservableList<Reaction> list = encounterReactionsList.getItems();
        int index = list.indexOf(selectedReaction);
        Reaction oldReaction = list.remove(index);
        list.add(index + 1, oldReaction);
        refresh();
        encounterReactionsList.getSelectionModel().selectIndices(index + 1);
    }

    private boolean confirm(String prompt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(prompt);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    private void refresh() {
        parent.refresh();
        encounterReactionsList.refresh();
    }

}
