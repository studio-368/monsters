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

import edu.bsu.storygame.editor.EditorStageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class EmptyDocumentPane extends VBox {

    private final EditorStageController controller;
    private final VBox links = new VBox(); {
        links.setAlignment(Pos.CENTER);
    }

    public EmptyDocumentPane(EditorStageController controller) {
        super();
        this.controller = controller;
        setStyle("-fx-background-color:#606060");
        setEffect(new InnerShadow(10.0, 2.0, 2.0, Color.BLACK));
        setAlignment(Pos.CENTER);
        setSpacing(14.0);
        setFillWidth(true);
        getChildren().add(links);
        addOpenHyperlinks();
    }

    private void addOpenHyperlinks() {
        addHyperlink("Open from JSON text...", event -> controller.openFromText());
        addHyperlink("Open file...", event -> controller.openFromFile());
    }

    private void addHyperlink(String text, EventHandler<ActionEvent> onAction) {
        Hyperlink link = new Hyperlink(text);
        link.setOnAction(onAction);
        links.getChildren().add(link);
    }


}
