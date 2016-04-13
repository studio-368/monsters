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

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;

public class JsonPromptStage extends Stage {

    private final AnchorPane root = new AnchorPane();
    private final TextArea jsonTextArea = new TextArea();
    private final HBox buttonHbox = new HBox();
    private final Button confirmButton = new Button("Open");
    private final Button cancelButton = new Button("Cancel");
    private final Hyperlink testLink = new Hyperlink("Test in web browser");

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
        stage.bindCopyButton();
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
        buttonHbox.getChildren().add(confirmButton);
        buttonHbox.getChildren().add(cancelButton);
    }

    private void configure() {
        cancelButton.setCancelButton(true);
        confirmButton.setDefaultButton(true);
        bindOkButton();
        bindCancelButton();
        setScene(new Scene(root));
    }

    private void bindOkButton() {
        confirmButton.setText("Open");
        confirmButton.setOnAction(event -> {
            jsonText = jsonTextArea.getText();
            close();
        });
    }

    private void bindCopyButton() {
        confirmButton.setText("Copy");
        confirmButton.setOnAction(event -> {
            jsonTextArea.copy();
            jsonTextArea.selectAll();
            confirmButton.setText("Copied!");
            displayTestLink();
        });
    }

    private void displayTestLink() {
        testLink.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URL("http://spring-studio-2016.github.io/monsters/?override").toURI());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        buttonHbox.getChildren().add(0, testLink);
    }

    private void bindCancelButton() {
        cancelButton.setOnAction(event -> close());
    }

}
