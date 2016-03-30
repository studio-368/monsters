package edu.bsu.storygame.editor.view;

import com.sun.javafx.collections.ObservableListWrapper;
import edu.bsu.storygame.editor.EditorStageController;
import edu.bsu.storygame.editor.model.Narrative;
import edu.bsu.storygame.editor.model.Region;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;

import java.io.IOException;

public class NarrativeEditPane extends EditPane {

    private final Narrative narrative;
    private final EditorStageController parent;

    @FXML
    private ListView<Region> narrativeRegionList;

    public NarrativeEditPane(Narrative narrative, EditorStageController parent) {
        this.narrative = narrative;
        this.parent = parent;
        loadFxml();
        configure();
    }

    private void loadFxml() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/NarrativeEdit.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configure() {
        narrativeRegionList.setItems(new ObservableListWrapper<>(narrative.regions));
        configureSelectionListener();
    }

    private void configureSelectionListener() {
        narrativeRegionList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Region>) change -> {
            parent.clearAfter(this);
            if (change.getList().size() == 1)
                parent.editRegion(change.getList().get(0));
        });
    }

    public void refresh() {
        narrativeRegionList.refresh();
    }


}
