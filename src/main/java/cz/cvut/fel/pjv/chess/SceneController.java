package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.players.LocalPlayer;
import cz.cvut.fel.pjv.chess.players.Player;
import cz.cvut.fel.pjv.chess.players.RemotePlayer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SceneController {
    private Stage stage;
    private Scene scene;

    public void switchToGame(ActionEvent event, Player white, Player black) {
        switchToGame(event, white, black, GameMode.LOCAL);
    }

    public void switchToGame(ActionEvent event, Player white, Player black, Board board) {
        final GameScene gs = new GameScene(white, black, GameMode.LOCAL, board);
        goToGameScene(event, gs);
    }

    public void switchToGame(ActionEvent event, Player white, Player black, GameMode gameMode) {
        final GameScene gs = new GameScene(white, black, gameMode);
        goToGameScene(event, gs);
    }

    public void switchToOnlineGame(ActionEvent event, LocalPlayer lp, RemotePlayer rp) {
        Player white = rp.getColor() == MyColor.WHITE ? rp : lp;
        Player black = rp.getColor() == MyColor.BLACK ? rp : lp;
        final GameScene gs = new GameScene(white, black, GameMode.ONLINE);
        goToGameScene(event, gs);
    }

    public void goToGameScene(ActionEvent event, GameScene gs){
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        GridPane gameScene = gs.createGameScene(stage);
        scene = new Scene(gameScene);
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.show();
        });
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
        Button startButton = new Button("Start");
        nameField.setOnAction((event) -> setConnection(event, nameField, startButton));
        startButton.setOnAction((ActionEvent e) -> setConnection(e, nameField, startButton));
        HBox hbox = new HBox(4, nameLabel, nameField, startButton);
        hbox.setPadding(new Insets(8));
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }

    private void setConnection(ActionEvent event, TextField nameField, Button startButton){
        final String userName = nameField.getText().strip();
        startButton.setDisable(true);
        nameField.setDisable(true);
        RemotePlayer rp = new RemotePlayer();
        rp.setAlertCallback(this::illegalNameAlert);
        rp.setStartGameCallback(() -> {
            LocalPlayer lp = new LocalPlayer(MyColor.getOppositeColor(rp.getColor()));
            lp.setName(userName);
            switchToOnlineGame(event, lp, rp);
        });
        rp.sendMMRequest(userName);
    }

    private void illegalNameAlert() {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle(" ");
            a.setHeaderText("Illegal name.");
            a.setContentText("Someone is already logged in with this name.");
            a.show();
        });
    }
}
