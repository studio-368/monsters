package edu.bsu.storygame.editor;

import edu.bsu.storygame.editor.model.Encounter;
import edu.bsu.storygame.editor.model.Narrative;
import edu.bsu.storygame.editor.model.Reaction;
import edu.bsu.storygame.editor.model.Region;
import edu.bsu.storygame.editor.view.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import react.Slot;
import react.Value;

import java.net.URL;
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
}