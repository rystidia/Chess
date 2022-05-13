package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.players.Player;
import cz.cvut.fel.pjv.chess.players.RemotePlayer;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SceneController {
    private Stage stage;
    private Scene scene;

    public void switchToGame(ActionEvent event, Player white, Player black) {
        switchToGame(event, white, black, false);
    }

    public void switchToGame(ActionEvent event, Player white, Player black, Board board) {
        final GameScene gs = new GameScene(white, black, board);
        goToGameScene(event, gs);
    }

    public void switchToGame(ActionEvent event, Player white, Player black, boolean createMode) {
        final GameScene gs = new GameScene(white, black, createMode);
        goToGameScene(event, gs);
    }

    public void goToGameScene(ActionEvent event, GameScene gs){
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

    public void switchToLoginWindow(ActionEvent event) {
        final HBox loginWindow = newLoginWindow();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loginWindow);
        stage.setScene(scene);
        stage.show();
    }

    private HBox newLoginWindow() {
        TextField nameField;
        Label nameLabel = new Label("Enter your name:");
        nameField = new TextField();
        nameField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                // 2. start server (if not already running) and client
                String userName = nameField.getText().strip();
                RemotePlayer rp = new RemotePlayer();
                rp.sendMMRequest();
                //TODO wait until connected and set white and black
                // then switch to game
                //switchToGame(event, white, black);
            }
        });
        Button startButton = new Button("Start");
        startButton.setOnAction((ActionEvent e) -> {
            // 2. as above
            String userName = nameField.getText().strip();
            RemotePlayer rp = new RemotePlayer();
            rp.sendMMRequest();
            //TODO
        });
        HBox hbox = new HBox(4, nameLabel, nameField, startButton);
        hbox.setPadding(new Insets(8));
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }
}
