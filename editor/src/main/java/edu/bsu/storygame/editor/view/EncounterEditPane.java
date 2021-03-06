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
import edu.bsu.storygame.editor.model.Encounter;
import edu.bsu.storygame.editor.model.Reaction;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class EncounterEditPane extends EditPane {

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
            parent.clearAfter(this);
            if (c.getList().size() == 1) {
                selectedReaction = c.getList().get(0);
                parent.editReaction(selectedReaction);
                setReactionButtonsDisabled(false);
            } else {
                selectedReaction = null;
                setReactionButtonsDisabled(true);
            }
        });
        bindTextField(encounterNameTextField, (v, o, n) -> onNameChange());
        bindTextField(encounterImage, (v, o, n) -> onImageChange());
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

    private void bindTextField(TextField field, ChangeListener<String> listener) {
        field.textProperty().addListener(listener);
    }

    private void populate() {
        encounterReactionsList.setItems(new ObservableListWrapper<>(encounter.reactions));
        refresh();
    }

    @FXML
    private void onNameChange() {
        encounter.name = encounterNameTextField.getText();
        encounterName.setText(encounter.name + " encounter");
        parent.refresh();
    }

    @FXML
    private void onImageChange() {
        encounter.image = encounterImage.getText();
    }

    @FXML
    private void onReactionAdd() {
        String reactionName = TextPrompt.emptyPrompt();
        if (reactionName == null) return;
        Reaction reaction = new Reaction(reactionName, new ArrayList<>());
        encounterReactionsList.getItems().add(reaction);
        parent.refresh();
    }

    @FXML
    private void onReactionDelete() {
        if (parent.confirm("Are you sure you want to delete this?")) {
            encounterReactionsList.getItems().remove(selectedReaction);
            refresh();
        }
    }


    @FXML
    private void onReactionRename() {
        selectedReaction.name = TextPrompt.promptFromString(selectedReaction.name);
        parent.refresh();
    }

    @FXML
    private void onReactionUpwards() {
        ObservableList<Reaction> list = encounterReactionsList.getItems();
        int index = list.indexOf(selectedReaction);
        Reaction oldReaction = list.remove(index);
        list.add(index - 1, oldReaction);
        parent.refresh();
        encounterReactionsList.getSelectionModel().selectIndices(index - 1);
    }

    @FXML
    private void onReactionDownwards() {
        ObservableList<Reaction> list = encounterReactionsList.getItems();
        int index = list.indexOf(selectedReaction);
        Reaction oldReaction = list.remove(index);
        list.add(index + 1, oldReaction);
        parent.refresh();
        encounterReactionsList.getSelectionModel().selectIndices(index + 1);
    }

    public void refresh() {
        encounterName.setText(encounter.name + " encounter");
        encounterNameTextField.setText(encounter.name);
        encounterImage.setText(encounter.image);
        encounterReactionsList.getProperties().put("listRecreateKey", Boolean.TRUE);
    }

}
