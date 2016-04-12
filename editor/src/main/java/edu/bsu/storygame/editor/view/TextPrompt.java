/*
 * Copyright 2016 Paul Gestwicki
 *
 * This file is part of Traveler's Notebook: Monster Tales
 *
 * Traveler's Notebook: Monster Tales is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Traveler's Notebook: Monster Tales is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Traveler's Notebook: Monster Tales.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.bsu.storygame.editor.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TextPrompt extends Stage {

    private final VBox root = new VBox();
    private final HBox buttonContainer = new HBox();
    private final TextField textField = new TextField();
    private final Button okButton = new Button("Okay");
    private final Button cancelButton = new Button("Cancel");
    private String string;

    public static String emptyPrompt() {
        return promptFromString(null);
    }

    public static String promptFromString(String string) {
        TextPrompt prompt = new TextPrompt(string);
        prompt.showAndWait();
        return prompt.string;
    }

    private TextPrompt(String origin) {
        super();
        this.string = origin;
        textField.setText(string);
        textField.selectAll();
        configure();
    }

    private void configure() {
        layout();
        setUpButtons();
        setScene(new Scene(root));
        setHeight(100.0);
        setWidth(400.0);
    }

    private void layout() {
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10.0);
        root.getChildren().add(textField);
        root.getChildren().add(buttonContainer);
        buttonContainer.setSpacing(10.0);
        buttonContainer.getChildren().add(cancelButton);
        buttonContainer.getChildren().add(okButton);
        buttonContainer.setAlignment(Pos.CENTER);
    }

    private void setUpButtons() {
        setUpOkButton();
        setUpCancelButton();
    }

    private void setUpOkButton() {
        okButton.setDefaultButton(true);
        okButton.setOnAction(event -> {
            string = textField.getText();
            close();
        });
    }

    private void setUpCancelButton() {
        cancelButton.setCancelButton(false);
        cancelButton.setOnAction(event -> close());
    }
}
