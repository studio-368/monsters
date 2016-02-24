package edu.bsu.storygame.editor;

import edu.bsu.storygame.editor.model.Encounter;
import edu.bsu.storygame.editor.model.Narrative;
import edu.bsu.storygame.editor.view.EmptyDocumentPane;
import edu.bsu.storygame.editor.view.EncounterEditPane;
import edu.bsu.storygame.editor.view.JsonPromptStage;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import react.Slot;
import react.Value;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class EditorStageController implements Initializable {

    @FXML
    private BorderPane rootNode;
    @FXML
    private MenuItem fileSaveMenuItem;
    @FXML
    public MenuItem textSaveMenuItem;
    @FXML
    private TreeView<String> narrativeTree;
    private EmptyDocumentPane emptyDocumentPane = new EmptyDocumentPane(this);
    private final Stack<Pane> editStack = new Stack<>();

    private final GsonParser parser = new GsonParser();
    private final Value<Narrative> narrative = Value.create(null);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureNarrativeListener();
        configureItemSelection();
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

    public void updateNarrativeView() {
        if (narrative.get() == null) {
            configureNoDocument();
            return;
        }
        TreeItemWrapper<Narrative> root = TreeItemWrapper.wrap(narrative.get());
        narrativeTree.setRoot(root);
        narrativeTree.setShowRoot(false);
        configureDocumentOpened();
    }

    private void configureNoDocument() {

    }

    private void configureDocumentOpened() {
        textSaveMenuItem.setDisable(false);
    }

    private void configureItemSelection() {
        narrativeTree.getSelectionModel().getSelectedItems().addListener((ListChangeListener<TreeItem<String>>) c -> {
            if (c.getList().size() == 0) return;
            Object selectedObject = ((TreeItemWrapper<Object>) c.getList().get(0)).reference;
            if (selectedObject instanceof Encounter) {
                setEditPane(new EncounterEditPane((Encounter) selectedObject, EditorStageController.this));
            }
        });
    }

    private void setEditPane(Pane pane) {
        rootNode.setCenter(pane);
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
        updateNarrativeView();
    }
}