package cz.cvut.fel.pjv.chess;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class MenuScene {
    private final Styles style = new Styles();

    public GridPane createMenuScene() {
        final SceneController sceneController = new SceneController();
        Button local = style.newButton("Human vs. human local");
        local.setOnAction(sceneController::switchToGame);
        Button ai = style.newButton("Human vs. AI local");

        GridPane root = new GridPane();
        root.setAlignment(Pos.BOTTOM_LEFT);
        int row = 0;
        root.add(local, 0, row++, 1, 1);
        root.add(ai, 0, row++, 1, 1);
        root.add(style.newButton("Human vs. human online"), 0, row++, 1, 1);
        root.add(style.newButton("Quit"), 0, row, 1, 1);
        root.setVgap(20);
        root.setPadding(new Insets(20));
        root.setMinSize(style.minWidth, style.minHeight);
        return root;
    }
}
