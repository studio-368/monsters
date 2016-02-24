package edu.bsu.storygame.editor.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class JsonPromptStage extends Stage {

    private final AnchorPane root = new AnchorPane();
    private final TextArea jsonTextArea = new TextArea();
    private final HBox buttonHbox = new HBox();
    private final Button okButton = new Button("Open");
    private final Button cancelButton = new Button("Cancel");

    private String jsonText = null;

    public static String prompt() {
        JsonPromptStage stage = new JsonPromptStage();
        stage.showAndWait();
        return stage.jsonText;
    }

    public static void display(String json) {
        JsonPromptStage stage = new JsonPromptStage();
        stage.jsonTextArea.setText(json);
        stage.jsonTextArea.setEditable(false);
        stage.show();
    }

    private JsonPromptStage() {
        super();
        layout();
        configure();
    }

    private void layout() {
        layoutJsonTextArea();
        layoutButtonContainer();
    }

    private void layoutJsonTextArea() {
        root.getChildren().add(jsonTextArea);
        AnchorPane.setLeftAnchor(jsonTextArea, 14.0);
        AnchorPane.setRightAnchor(jsonTextArea, 14.0);
        AnchorPane.setTopAnchor(jsonTextArea, 10.0);
        AnchorPane.setBottomAnchor(jsonTextArea, 58.0);
        jsonTextArea.setPromptText("Enter JSON text...");
    }

    private void layoutButtonContainer() {
        root.getChildren().add(buttonHbox);
        buttonHbox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        buttonHbox.setPrefHeight(30.0);
        buttonHbox.setSpacing(10.0);
        AnchorPane.setBottomAnchor(buttonHbox, 14.0);
        AnchorPane.setRightAnchor(buttonHbox, 14.0);
        buttonHbox.getChildren().add(okButton);
        buttonHbox.getChildren().add(cancelButton);
    }

    private void configure() {
        cancelButton.setCancelButton(true);
        okButton.setDefaultButton(true);
        bindOkButton();
        bindCancelButton();
        setScene(new Scene(root));
    }

    private void bindOkButton() {
        okButton.setOnAction(event -> {
            jsonText = jsonTextArea.getText();
            close();
        });
    }

    private void bindCancelButton() {
        cancelButton.setOnAction(event -> close());
    }

}
