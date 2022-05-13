package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.players.AIPlayer;
import cz.cvut.fel.pjv.chess.players.LocalPlayer;
import cz.cvut.fel.pjv.chess.players.Player;
import cz.cvut.fel.pjv.chess.players.RemotePlayer;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuScene {

    Player white;
    Player black;
    private final SceneController sceneController = new SceneController();
    private final Styles style = new Styles();

    public GridPane createMenuScene() {

        Button local = style.newButton("Human vs. human local");
        local.setOnAction(evt -> sceneController.switchToGame(evt, new LocalPlayer(MyColor.WHITE), new LocalPlayer(MyColor.BLACK)));
        Button ai = style.newButton("Human vs. AI local");
        ai.setOnAction(this::AImode);
        Button online = style.newButton("Human vs. human online");
        online.setOnAction(sceneController::switchToLoginWindow);
        GridPane root = new GridPane();
        root.setAlignment(Pos.BOTTOM_LEFT);
        int row = 0;
        root.add(local, 0, row++, 1, 1);
        root.add(ai, 0, row++, 1, 1);
        root.add(online, 0, row++, 1, 1);
        Button quit = style.newButton("Quit");
        root.add(quit, 0, row, 1, 1);
        quit.setOnAction(this::closeStage);
        root.setVgap(20);
        root.setPadding(new Insets(20));
        root.setMinSize(style.minWidth, style.minHeight);
        return root;
    }

    public void AImode(ActionEvent event){
        colorChoosingDialog();
        sceneController.switchToGame(event, white, black);
    }

    public void closeStage(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    private void colorChoosingDialog(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Choose your color");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        Button applyButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.APPLY);
        applyButton.setVisible(false);

        VBox vbox = new VBox();
        vbox.getChildren().add(newColorChooseButton(MyColor.WHITE, applyButton));
        vbox.getChildren().add(newColorChooseButton(MyColor.BLACK, applyButton));

        dialog.setOnCloseRequest(evt -> dialog.close());
        dialog.getDialogPane().setContent(vbox);
        dialog.showAndWait();
        dialog.close();
    }

    private Button newColorChooseButton(MyColor color, Button applyButton){
        String colorString = color == MyColor.WHITE ? "White" : "Black";
        Button button = style.newButton(colorString);
        button.setOnAction(e -> {
            if (color == MyColor.WHITE){
                white = new LocalPlayer(MyColor.WHITE);
                black = new AIPlayer(MyColor.BLACK);
            } else {
                black = new LocalPlayer(MyColor.BLACK);
                white = new AIPlayer(MyColor.WHITE);
            }
            applyButton.fire();
        });
        return button;
    }

}
