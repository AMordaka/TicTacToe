package menu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import menu.controller.Controller;

public class Menu extends Application{

    private final String LAYOUT_ADDRESS = "view/menuLayout.fxml";
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource(LAYOUT_ADDRESS));
        StackPane pane = loader.load();
        stage = primaryStage;
        Controller controller = loader.getController();
        controller.setStage(stage);
        stage.setTitle("Tic Tac Toe - powered by Arkadiusz Mordaka");
        stage.setScene(new Scene(pane, 400, 600));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
