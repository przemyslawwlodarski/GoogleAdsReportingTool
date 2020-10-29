package com.pfx.demo;

import com.pfx.demo.app.BadInput;
import com.pfx.demo.app.Exit;
import com.pfx.demo.app.KeyWordsBrAndCost;
import com.pfx.demo.keywords.GetDuplicateKeywords;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

//3534529120
public class FinalApplication extends Application {

    private Stage window;
    private Long googleAdsAccountId;
    private TextField accountId;
    ComboBox reportType;


    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {

        //Scene 1
        window = primaryStage;
        window.setTitle("Pij kawę, będziesz wielki");
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        //Scene 1 Buttons
        Button nextButton = new Button("Dalej");
        nextButton.setOnAction(e -> {
            if (isLong(accountId)) {
                googleAdsAccountId = Long.parseLong(accountId.getText().replace("-",""));
                if (reportType.getValue().equals("Słowa kluczowe z wysokim współczynnikiem odrzuceń oraz wysokim kosztem")){
                    KeyWordsBrAndCost.display(googleAdsAccountId);
                } else if (reportType.getValue().equals("Znajdź aktywne duplikaty słów kluczowych")){
                    GetDuplicateKeywords.getKeywords(googleAdsAccountId);
                } else {
                    BadInput.display("Error", "Wybierz raport");
                }
            } else {
                BadInput.display("Error", "Podaj prawidłowy numer konta");
            }
        });
        //Scene 1 ComboBox
        reportType = new ComboBox<>();
        reportType.setPromptText("Wybierz raport");
        reportType.getItems().addAll(
                "Słowa kluczowe z wysokim współczynnikiem odrzuceń oraz wysokim kosztem",
                "Znajdź aktywne duplikaty słów kluczowych",
                "Raport 3",
                "Raport 4",
                "Raport 5",
                "Raport 6"
        );


        //Scene 1 Inputs
        accountId = new TextField ();
        accountId.setPromptText("Podaj ID konta");

        //Scene 1 Labels
        Label chooseAccountLabel = new Label("Podaj ID konta");
        Label chooseReportLabel = new Label("Wybierz raport");

        GridPane layout1 = new GridPane();
        layout1.setPadding(new Insets(10, 10, 10, 10));
        layout1.setVgap(5);
        layout1.setHgap(5);
        accountId.setPrefColumnCount(10);
        GridPane.setConstraints(accountId, 4, 6);
        GridPane.setConstraints(nextButton, 6, 6);
        GridPane.setConstraints(chooseAccountLabel, 4, 5);
        GridPane.setConstraints(reportType, 5, 6);
        GridPane.setConstraints(chooseReportLabel, 5, 5);


        layout1.getChildren().addAll(accountId,nextButton,chooseAccountLabel, reportType, chooseReportLabel);
        layout1.setAlignment(Pos.CENTER);


        Scene scene1 = new Scene(layout1, 750, 150);
        primaryStage.setScene(scene1);
        primaryStage.show();
    }


    private void closeProgram() {
        boolean answer = Exit.display("Confirm Box", "Czy na pewno chcesz wyjść?");
        if (answer)
            window.close();
    }
    private boolean isLong(TextField input){
        try {
            long x = Long.parseLong(input.getText().replace("-",""));
            if (x >= 1)
                return true;
            else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
