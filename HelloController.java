package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.demo.WelcomeController.WINNER;
public class HelloController implements Initializable {
    @FXML
    private Text winnerField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            winnerField.setText(WINNER + " IS THE WINNER");
    }
}