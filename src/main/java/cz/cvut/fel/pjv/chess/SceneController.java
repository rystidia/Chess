package cz.cvut.fel.pjv.chess;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SceneController {
    private Stage stage;
    private Scene scene;

    public void switchToGame(ActionEvent event) {
        final GameScene gs = new GameScene();
        GridPane gameScene = gs.createGameScene();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(gameScene);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMenu(ActionEvent event) {
        final MenuScene ms = new MenuScene();
        GridPane menuScene = ms.createMenuScene();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(menuScene);
        stage.setScene(scene);
        stage.show();
    }
}
