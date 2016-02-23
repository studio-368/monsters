package edu.bsu.storygame.editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Editor extends Application {

    private EditorStageController controller = new EditorStageController();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage editorStage) throws Exception {
        configure(editorStage);
        editorStage.show();
    }

    private void configure(Stage editorStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/EditorStage.fxml"));
        loader.setController(controller);
        editorStage.setScene(new Scene(loader.load()));
    }
}
