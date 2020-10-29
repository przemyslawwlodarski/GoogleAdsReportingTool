package com.pfx.demo;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication extends Application {
    private Stage window;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        /*//Scene 1
        window = primaryStage;
        window.setTitle("Lets the war begin");
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        */
    }
     /*
    private void closeProgram() {
        boolean answer = GameExit.display("Confirm Box", "Do you want to exit a game?");
        if (answer)
            window.close();

    }
     */
}