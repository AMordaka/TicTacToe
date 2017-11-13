package menu.controller;

import game.TicTacToeClient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    @FXML
    private Button start;

    @FXML
    private Button info;

    @FXML
    private Button exit;

    Stage stage;

    @FXML
    void initialize() throws IOException {
        startMethod();
        infoMethod();
        exitMethod();
    }

    void startMethod(){
        start.setOnAction(event -> {
            new Thread(() -> {
                while (true) {
                    try {
                        TicTacToeClient.main(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            stage.hide();
        });
    }

    void infoMethod(){
        info.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tic Tac Toe");
            alert.setHeaderText(null);
            alert.setContentText("Gra Kółko i krzyżyk przeznaczona jest dla 3 graczy, \n" +
                    "stworzona przez studenta Politechniki Łódzkiej na zaliczenie przedmiotu: Projektowanie Aplikacji Internetowych\n\n" +
                    "ZASADY GRY:\n" +
                    "Gracz aby wygrać musi ułożyć pięć takich samych figur w pionie/poziomie lub po skosie");

            alert.showAndWait();
        });

    }

    void exitMethod(){
        exit.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("TicTacToe");
            alert.setHeaderText("Czy na pewno chcesz wyjść?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.exit(1);
            } else {
                alert.close();
            }
        });
    }

    public void setStage(Stage stage){this.stage = stage;}


}
