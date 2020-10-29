package com.pfx.demo.app;

import com.pfx.demo.keywords.GetKeywordBrAndCost;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class KeyWordsBrAndCost {

    private static TextField bounceRateLevel;
    private static TextField costLevel;
    private static double finalBounceRateLevel;
    private static double finalCostLevel;


    public static void display(Long googleAdsAccountId) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);

        Button generateReportButton = new Button("Generuj raport");
        generateReportButton.setOnAction(e -> {
            if (isDouble(bounceRateLevel)&&isDouble(costLevel)) {
                finalBounceRateLevel = Double.parseDouble(bounceRateLevel.getText());
                finalCostLevel = Double.parseDouble(costLevel.getText());
                try{
                    new GetKeywordBrAndCost().getKeyWords(googleAdsAccountId, finalBounceRateLevel, finalCostLevel);
                } catch (Exception exception){
                    BadInput.display("Error", "Podaj prawidłowy numer konta");
                }
            } else {
                BadInput.display("Error", "Podałeś złe warunki liczbowe");
            }
        });

        //Labels
        Label labelBr = new Label("Podaj graniczny poziom BR za ostatnie 7 dni");
        Label labelCost = new Label("Podaj minimalny koszt słowa kluczowego w ostatnich 7 dniach");

        //Inputs
        bounceRateLevel = new TextField ();
        bounceRateLevel.setPromptText("Podaj BR");
        costLevel = new TextField ();
        costLevel.setPromptText("Podaj koszt");


        GridPane layout1 = new GridPane();
        layout1.setPadding(new Insets(10, 10, 10, 10));
        layout1.setVgap(5);
        layout1.setHgap(5);
        GridPane.setConstraints(bounceRateLevel, 5, 6);
        GridPane.setConstraints(labelBr, 5, 5);
        GridPane.setConstraints(costLevel, 6, 6);
        GridPane.setConstraints(labelCost, 6, 5);
        GridPane.setConstraints(generateReportButton, 7, 6);

        layout1.getChildren().addAll(bounceRateLevel,labelBr,costLevel, labelCost, generateReportButton);
        layout1.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout1, 750, 250);
        window.setScene(scene);
        window.showAndWait();
    }

    private static boolean isDouble(TextField input) {
        try {
            double x = Double.parseDouble(input.getText());
            if (x >= 0)
                return true;
            else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

}

