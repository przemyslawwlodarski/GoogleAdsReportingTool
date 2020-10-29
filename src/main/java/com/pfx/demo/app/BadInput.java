package com.pfx.demo.app;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BadInput {

    public static void display(String title, String message) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Button yesButton = new Button("Przepraszam");
        Label label = new Label(message);

        yesButton.setOnAction(e -> {
            window.close();
        });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, yesButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 250, 250);
        window.setScene(scene);
        window.showAndWait();
    }
}
