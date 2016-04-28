/*
 * Copyright 2016 Traveler's Notebook: Monster Tales project authors
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

package edu.bsu.storygame.editor;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class FilePrompt {
    private final FileChooser chooser = new FileChooser();
    private final Window parent;

    public FilePrompt(Window parent) {
        this.parent = parent;
    }

    public String open() {
        chooser.setTitle("Open file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Monsters JSON file", "*.json"));
        chooser.setInitialFileName("");
        File file = chooser.showOpenDialog(parent);
        try {
            return read(file);
        } catch (IOException e) {
            handleReadError(e);
        }
        return null;
    }

    private String read(File file) throws IOException {
        if (file == null) return null;
        byte[] bytes = Files.readAllBytes(file.toPath());
        String string = new String(bytes, Charset.defaultCharset());
        return string;
    }

    private void handleReadError(IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error opening document");
        alert.setContentText("There was an error reading the selected document. Please try again with another file.");
        alert.showAndWait();
    }

    public void save(String json) {
        chooser.setTitle("Save file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Monsters JSON file", "*.json"));
        chooser.setInitialFileName("narrative.json");
        File file = chooser.showSaveDialog(parent);
        try {
            write(json, file);
        } catch (IOException e) {
            handleWriteError(e);
        }
    }

    private void write(String json, File file) throws IOException {
        if (file == null) return;
        Files.write(file.toPath(), json.getBytes(Charset.defaultCharset()), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }

    private void handleWriteError(IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error saving document");
        alert.setContentText("There was an error saving the document. Please try saving in another location.");
        alert.showAndWait();
    }
}
