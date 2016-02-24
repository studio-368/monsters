package edu.bsu.storygame.editor.view;

import com.sun.javafx.collections.ObservableListWrapper;
import edu.bsu.storygame.editor.model.Encounter;
import edu.bsu.storygame.editor.model.Reaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class EncounterEditPane extends GridPane {

    private final Encounter encounter;

    @FXML
    private ListView<Reaction> encounterReactionsList;
    @FXML
    private Label encounterName;
    @FXML
    private Button reactionAddButton;
    @FXML
    private TextField encounterNameTextField;
    @FXML
    private TextField encounterImage;
    @FXML
    private Button reactionDeleteButton;
    @FXML
    private Button reactionOpenButton;

    public EncounterEditPane(Encounter encounter) {
        this.encounter = encounter;
        loadFxml();
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

    private void populate() {
        encounterName.setText(encounter.name + " encounter");
        encounterNameTextField.setText(encounter.name);
        encounterReactionsList.setItems(new ObservableListWrapper<>(encounter.reactions));
        encounterImage.setText(encounter.image);
    }

    @FXML
    private void onReactionAdd() {
        // TODO This method must exist for FXML to load
    }

    @FXML
    private void onReactionDelete() {
        // TODO This method must exist for FXML to load
    }

    @FXML
    private void onReactionOpen() {
        // TODO This method must exist for FXML to load
    }

}
