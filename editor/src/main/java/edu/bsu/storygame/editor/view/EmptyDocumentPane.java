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

import edu.bsu.storygame.editor.EditorStageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Hyperlink;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class EmptyDocumentPane extends VBox {

    private final EditorStageController controller;
    private final Group links = new Group();

    public EmptyDocumentPane(EditorStageController controller) {
        super();
        this.controller = controller;
        setStyle("-fx-background-color:#606060");
        setEffect(new InnerShadow(10.0, 2.0, 2.0, Color.BLACK));
        setAlignment(Pos.CENTER);
        setSpacing(14.0);
        getChildren().add(links);
        addOpenFromTextHyperlink();
    }

    private void addOpenFromTextHyperlink() {
        addHyperlink("Open from JSON text...", event -> controller.openFromText());
    }

    private void addHyperlink(String text, EventHandler<ActionEvent> onAction) {
        Hyperlink link = new Hyperlink(text);
        link.setOnAction(onAction);
        links.getChildren().add(link);
    }


}
