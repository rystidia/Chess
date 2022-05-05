package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;
import cz.cvut.fel.pjv.chess.players.LocalPlayer;
import cz.cvut.fel.pjv.chess.players.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GameScene extends GridPane {

    private final Styles style = new Styles();
    private final SceneController sceneController = new SceneController();
    private GridPane boardTable;
    private Figure figureBeingMoved;
    private final Player white = new LocalPlayer(MyColor.WHITE);
    private final Player black = new LocalPlayer(MyColor.BLACK);
    private final GameController gc = new GameController(white, black);
    Thread timer;

    public GridPane createGameScene() {
        Board test = new Board();
        test.initialPosition();
        figureBeingMoved = null;

//        white = new LocalPlayer(MyColor.WHITE);
//        black = new LocalPlayer(MyColor.BLACK);

        white.setCurrentPlayer(true);
        black.setCurrentPlayer(false);
        white.setTimeLeft(25 * 60);
        black.setTimeLeft(25 * 60);

        GridPane table = new GridPane();
        for (int i = 0; i < 8; i++) {
            table.add(style.newRowLabel(i), 0, i + 1, 1, 1);
            table.add(style.newRowLabel(i), 9, i + 1, 1, 1);
            table.add(style.newColLabel(i), i + 1, 0, 1, 1);
            table.add(style.newColLabel(i), i + 1, 9, 1, 1);
        }
        boardTable = drawBoard(test);
        table.add(boardTable, 1, 1, 8, 8);
        table.setAlignment(Pos.CENTER);

        // vert menu
        VBox leftVertMenu = new VBox();
        VBox options = newOptions();
        leftVertMenu.setPadding(new Insets(15));
        BorderPane timeWhite = newClockBox(white);
        BorderPane timeBlack = newClockBox(black);
        leftVertMenu.getChildren().addAll(timeBlack, options, timeWhite);
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.add(table, 0, 0, 1, 1);
        root.add(leftVertMenu, 1, 0, 1, 1);
        root.setMinSize(style.minWidth, style.minHeight);

        if (timer != null) timer.stop();
        Clock clock = new Clock(white, black, timeWhite, timeBlack);
        timer = new Thread(clock);
        timer.setDaemon(true);
        timer.start();
        return root;
    }

    private GridPane drawBoard(Board board) {
        return drawBoard(board, new HashSet<>());
    }

    private GridPane drawBoard(Board board, Set<Field> validMoves) {
        GridPane grid = new GridPane();
        ColumnConstraints columnConstraints = new ColumnConstraints(style.size);
        RowConstraints rowConstraints = new RowConstraints(style.size);

        Field figPos = figureBeingMoved != null ? figureBeingMoved.getPosition() : null;
        for (int r = 0; r <= Board.MAX_ROW; r++) {
            grid.getColumnConstraints().add(columnConstraints);
            grid.getRowConstraints().add(rowConstraints);
            for (int c = 0; c <= Board.MAX_COL; c++) {
                final Field fieldPos = new Field(r, c);
                StackPane field = new StackPane();
                Figure fig = board.getFigure(fieldPos);

                Background white = validMoves.contains(fieldPos) || Objects.equals(fieldPos, figPos) ? style.WHITE_GREEN : style.WHITE;
                Background brown = validMoves.contains(fieldPos) || Objects.equals(fieldPos, figPos) ? style.BROWN_GREEN : style.BROWN;
                if (fig != null && figureBeingMoved == null && gc.isCurrentColor(fig.getColor())) {
                    white = fig.hasValidMoves() ? style.WHITE_YELLOW : style.WHITE;
                    brown = fig.hasValidMoves() ? style.BROWN_YELLOW : style.BROWN;
                }

                field.setBackground(((r + c) & 1) == 0 ? white : brown);
                ImageView image = style.getImageFigure(fig);
                if (image != null) {
                    field.getChildren().add(image);
                }
                grid.add(field, c, r); // intentionally (c, r)

                field.setOnMouseClicked(evt -> {
                    GridPane updatedBoard;
                    if (fig != null && fig == figureBeingMoved) return;
                    if (figureBeingMoved == null) {
                        if (fig == null || !gc.isCurrentColor(fig.getColor()) || !fig.hasValidMoves()) return;
                        figureBeingMoved = fig;
                        Set<Field> figValidMoves = board.getValidMoves(fig);
                        updatedBoard = drawBoard(board, figValidMoves);
                    } else {
                        Set<Field> movedFigureValidMoves = board.getValidMoves(figureBeingMoved);
                        if (movedFigureValidMoves.contains(fieldPos)) {
                            board.moveFigure(figureBeingMoved, fieldPos);
                            if (figureBeingMoved instanceof Pawn && ((Pawn) figureBeingMoved).moveLeadsToPromotion(fieldPos)) {
                                promotionDialog((Pawn) figureBeingMoved, board);
                            }
                            gc.switchCurPlayer();
                            figureBeingMoved = null;
                        }
                        updatedBoard = drawBoard(board);
                    }
                    redrawBoard(updatedBoard);
                });
            }
        }
        return grid;
    }

    private BorderPane newClockBox(Player player) {
        BorderPane clockBox = new BorderPane();
        Label playerName = style.newLabel("PlayerName", 20);
        Label playerTime = style.newLabel(player.getTimeString(), 30);
        clockBox.setTop(playerName);
        clockBox.setCenter(playerTime);
        clockBox.setBorder(new Border((new BorderStroke(Color.BLACK,
            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
        clockBox.setPadding(new Insets(10));
        return clockBox;
    }

    private VBox newOptions() {
        VBox options = new VBox();
        Button menu = style.newButton("Menu");
        menu.setOnAction(sceneController::switchToMenu);
        Button restart = style.newButton("Restart");
        restart.setOnAction(sceneController::switchToGame);
        Button save = style.newButton("Save");
        Button load = style.newButton("Load");
        Button create = style.newButton("Create");
        options.setSpacing(10);
        options.setPadding(new Insets(25, 0, 25, 0));
        options.getChildren().addAll(menu, restart, save, load, create);
        return options;
    }

    private void redrawBoard(GridPane newBoardTable) {
        GridPane parentTable = (GridPane) boardTable.getParent();
        ((GridPane) boardTable.getParent()).getChildren().remove(boardTable);
        parentTable.add(newBoardTable, 1, 1, 8, 8);
        boardTable = newBoardTable;
    }

    public void promotionDialog(Pawn pawn, Board board) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Promotion");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        Button applyButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.APPLY);
        applyButton.setVisible(false);

        VBox vbox = new VBox();
        vbox.getChildren().add(newPromotionButton(new Queen(pawn.getColor(), board), pawn, applyButton));
        vbox.getChildren().add(newPromotionButton(new Knight(pawn.getColor(), board), pawn, applyButton));
        vbox.getChildren().add(newPromotionButton(new Rook(pawn.getColor(), board), pawn, applyButton));
        vbox.getChildren().add(newPromotionButton(new Bishop(pawn.getColor(), board), pawn, applyButton));

        dialog.setOnCloseRequest(evt -> {
                if (applyButton.isPressed())
                    pawn.promotion(new Queen(pawn.getColor(), board));
            }
        );
        dialog.getDialogPane().setContent(vbox);
        dialog.showAndWait();
        dialog.close();
    }

    public Button newPromotionButton(Figure figure, Pawn pawn, Button applyButton) {
        Button button = style.newButton(figure.getClass().getSimpleName());
        button.setOnAction(e -> {
            pawn.promotion(figure);
            applyButton.fire();
        });
        return button;
    }
}
