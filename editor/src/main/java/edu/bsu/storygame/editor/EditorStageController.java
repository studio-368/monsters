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
import edu.bsu.storygame.editor.view.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import react.Slot;
import react.Value;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditorStageController implements Initializable {

    @FXML
    private BorderPane rootNode;
    @FXML
    private MenuItem fileSaveMenuItem;
    @FXML
    public MenuItem textSaveMenuItem;
    private VBox editArea = new VBox();
    private ScrollPane editScrollBar = new ScrollPane(editArea);

    {
        editScrollBar.setFitToWidth(true);
    }
    private EmptyDocumentPane emptyDocumentPane = new EmptyDocumentPane(this);

    private final GsonParser parser = new GsonParser();
    private final Value<Narrative> narrative = Value.create(null);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootNode.setCenter(editScrollBar);
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
        if (narrative.get() == null)
            configureNoDocument();
        else
            configureDocumentOpened();
    }

    private void configureNoDocument() {

    }

    private void configureDocumentOpened() {
        clear();
        editArea.getChildren().clear();
        add(new NarrativeEditPane(narrative.get(), this));
        textSaveMenuItem.setDisable(false);
    }

    public void editRegion(Region region) {
        add(new RegionEditPane(region, this));
    }

    public void editEncounter(Encounter encounter) {
        add(new EncounterEditPane(encounter, this));
    }

    public void editReaction(Reaction reaction) {
        add(new ReactionEditPane(reaction, this));
    }

    public void editStory(Story story) {
        add(new StoryEditPane(story, this));
    }


    @FXML
    public void openFromText() {
        String jsonString = JsonPromptStage.prompt();
        if (jsonString == null) return;
        narrative.update(parser.parse(jsonString));
    }

    @FXML
    public void displayJsonText() {
        String jsonString = parser.convertToJson(narrative.get());
        JsonPromptStage.display(jsonString);
    }

    public void refresh() {
        for (Node node : editArea.getChildren())
            ((EditPane) node).refresh();
    }

    public void add(EditPane pane) {
        editArea.getChildren().add(pane);
    }

    private void clear() {
        editArea.getChildren().clear();
    }

    public void clearAfter(EditPane pane) {
        int startIndex = editArea.getChildren().indexOf(pane) + 1;
        int endIndex = editArea.getChildren().size();
        if (startIndex != 0 && endIndex - startIndex > 0)
            editArea.getChildren().remove(startIndex, endIndex);
    }

    public boolean confirm(String prompt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(prompt);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
