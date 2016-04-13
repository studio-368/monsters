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
import edu.bsu.storygame.editor.model.Region;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class RegionEditPane extends GridPane {
    private final Region region;
    private final EditorStageController parent;

    @FXML
    private Label regionName;
    @FXML
    private ListView<Encounter> regionEncountersList;
    @FXML
    private Button encounterAddButton;
    @FXML
    private Button encounterDeleteButton;
    @FXML
    private Button encounterRenameButton;
    @FXML
    private Button encounterUpButton;
    @FXML
    private Button encounterDownButton;
    private Encounter selectedEncounter = null;

    public RegionEditPane(Region region, EditorStageController parent) {
        this.region = region;
        this.parent = parent;
        loadFxml();
        configure();
        populate();
    }

    private void loadFxml() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/RegionEdit.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configure() {
        setOnEncounterSelectionChanged(change -> {
            if (change.getList().size() == 0) {
                selectedEncounter = null;
                setEncounterButtonsDisabled(true);
            } else {
                selectedEncounter = change.getList().get(0);
                setEncounterButtonsDisabled(false);
            }
        });
    }

    private void setOnEncounterSelectionChanged(ListChangeListener<Encounter> listener) {
        regionEncountersList.getSelectionModel().getSelectedItems().addListener(listener);
    }

    private void setEncounterButtonsDisabled(boolean disabled) {
        ObservableList<Encounter> list = regionEncountersList.getItems();
        boolean topOfList = list.indexOf(selectedEncounter) == 0;
        boolean bottomOfList = list.indexOf(selectedEncounter) == list.size() - 1;
        encounterDeleteButton.setDisable(disabled);
        encounterRenameButton.setDisable(disabled);
        encounterUpButton.setDisable(disabled || topOfList);
        encounterDownButton.setDisable(disabled || bottomOfList);
    }

    private void populate() {
        regionName.setText("Region " + region.region);
        regionEncountersList.setItems(new ObservableListWrapper<>(region.encounters));
    }

    @FXML
    private void onEncounterAdd() {
        String encounterName = TextPrompt.emptyPrompt();
        if (encounterName == null) return;
        Encounter encounter = new Encounter(encounterName, "", new ArrayList<>());
        regionEncountersList.getItems().add(encounter);
        refresh();
    }

    @FXML
    private void onEncounterRename() {
        selectedEncounter.name = TextPrompt.promptFromString(selectedEncounter.name);
        refresh();
    }

    @FXML
    private void onEncounterDelete() {
        if (confirm("Are you sure you want to delete this?")) {
            regionEncountersList.getItems().remove(selectedEncounter);
            refresh();
        }
    }

    @FXML
    private void onEncounterUpwards() {
        ObservableList<Encounter> list = regionEncountersList.getItems();
        int index = list.indexOf(selectedEncounter);
        Encounter encounter = list.remove(index);
        list.add(index - 1, encounter);
        refresh();
        regionEncountersList.getSelectionModel().selectIndices(index - 1);
    }

    @FXML
    private void onEncounterDownwards() {
        ObservableList<Encounter> list = regionEncountersList.getItems();
        int index = list.indexOf(selectedEncounter);
        Encounter encounter = list.remove(index);
        list.add(index + 1, encounter);
        refresh();
        regionEncountersList.getSelectionModel().selectIndices(index + 1);
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
        regionEncountersList.refresh();
    }
}
