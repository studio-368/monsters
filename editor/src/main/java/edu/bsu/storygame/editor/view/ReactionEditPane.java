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

import com.sun.javafx.collections.ObservableListWrapper;
import edu.bsu.storygame.editor.EditorStageController;
import edu.bsu.storygame.editor.model.Reaction;
import edu.bsu.storygame.editor.model.Story;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ReactionEditPane extends EditPane {
    private final Reaction reaction;
    private final EditorStageController parent;
    private Story selectedStory;

    @FXML
    private Label reactionNameLabel;
    @FXML
    private TextField reactionNameTextField;
    @FXML
    private Label storyCountLabel;
    @FXML
    private ListView<Story> storyListView;
    @FXML
    private Button deleteStoryButton;

    public ReactionEditPane(Reaction reaction, EditorStageController parent) {
        this.reaction = reaction;
        this.parent = parent;
        loadFxml();
        configure();
    }

    private void loadFxml() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/ReactionEdit.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configure() {
        configureSelectionListener();
        storyListView.setItems(new ObservableListWrapper<>(reaction.stories));
        reactionNameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                reaction.name = newValue;
                reactionNameLabel.setText(newValue + " reaction");
                parent.refresh();
            }
        });
        refresh();
    }

    private void configureSelectionListener() {
        storyListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Story>) change -> {
            parent.clearAfter(this);
            if (change.getList().size() == 1) {
                selectedStory = change.getList().get(0);
                deleteStoryButton.setDisable(storyListView.getItems().size() <= 1);
                parent.editStory(selectedStory);
            } else {
                selectedStory = null;
                deleteStoryButton.setDisable(true);
            }
        });
    }

    @FXML
    private void onNewStory() {
        Story story = Story.emptyStory();
        storyListView.getItems().add(story);
        storyListView.getSelectionModel().select(story);
    }

    @FXML
    private void onDeleteStory() {
        if (parent.confirm("This will delete the story below. This cannot be undone. Are you sure you want to do this?\n\nStory:\n" + selectedStory.text)) {
            storyListView.getItems().remove(selectedStory);
        }
    }

    @Override
    public void refresh() {
        reactionNameLabel.setText(reaction.name);
        reactionNameTextField.setText(reaction.name);
        storyCountLabel.setText(reaction.stories.size() +
                reaction.stories.size() == 1 ? " story" : " stories"
        );
        storyListView.getProperties().put("listRecreateKey", Boolean.TRUE);
    }
}
