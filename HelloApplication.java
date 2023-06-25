package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;


import static com.example.demo.WelcomeController.BOARD_SIZE;
import static com.example.demo.WelcomeController.color;


public class HelloApplication extends Application {


    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = null;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("WelcomeScreen.fxml")));
        Scene scene = new Scene(root, 843, 811);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        displaySquareColors();
    }
    private static void displaySquareColors() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                System.out.print(color[row][col] + " ");
            }
            System.out.println();
        }
    }
}

