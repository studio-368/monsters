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
