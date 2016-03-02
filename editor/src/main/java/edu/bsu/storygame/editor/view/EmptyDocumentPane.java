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
