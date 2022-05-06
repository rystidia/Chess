package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;
import cz.cvut.fel.pjv.chess.players.AIPlayer;
import cz.cvut.fel.pjv.chess.players.LocalPlayer;
import cz.cvut.fel.pjv.chess.players.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GameScene extends GridPane {

    private final Styles style = new Styles();
    private final SceneController sceneController = new SceneController();
    private final Player white;
    private final Player black;
    private final GameController gc;
    Thread timer;
    private GridPane boardTable;
    private Figure figureBeingMoved;
    private boolean createMode = false;

    public GameScene(Player white, Player black) {
        this.white = white;
        this.black = black;
        this.gc = new GameController(white, black);
    }

    public GridPane createGameScene() {
        Board test = new Board();
        test.initialPosition();
        figureBeingMoved = null;

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

        Clock clock = new Clock(white, black, timeWhite, timeBlack);
        timer = new Thread(clock);
        timer.setDaemon(true);
        timer.start();

        return root;
    }

    public void setCreateMode(boolean createMode) {
        this.createMode = createMode;
    }

    private GridPane drawBoard(Board board) {
        return drawBoard(board, new HashSet<>());
    }

    private GridPane drawBoard(Board board, Set<Field> validMoves) {
        GridPane grid = new GridPane();

        Field figPos = figureBeingMoved != null ? figureBeingMoved.getPosition() : null;
        for (int r = 0; r <= Board.MAX_ROW; r++) {
            for (int c = 0; c <= Board.MAX_COL; c++) {
                final Field fieldPos = new Field(r, c);
                Label field = new Label();
                field.setMinSize(style.size, style.size);
                field.setMaxSize(style.size, style.size);
                Figure fig = board.getFigure(fieldPos);

                String white = validMoves.contains(fieldPos) || Objects.equals(fieldPos, figPos) ? style.WHITE_GREEN : style.WHITE;
                String brown = validMoves.contains(fieldPos) || Objects.equals(fieldPos, figPos) ? style.BROWN_GREEN : style.BROWN;
                if (fig != null && figureBeingMoved == null && gc.isCurrentColor(fig.getColor())) {
                    white = fig.hasValidMoves() ? style.WHITE_YELLOW : style.WHITE;
                    brown = fig.hasValidMoves() ? style.BROWN_YELLOW : style.BROWN;
                }

                field.setStyle(((r + c) & 1) == 0 ? white : brown);

                ImageView image = style.getImageFigure(fig);
                if (image != null) {
                    field.setGraphic(image);
                }

                if (createMode) {
                    field.setContextMenu(new CreateMenu(board, fieldPos));
                }

                grid.add(field, c, r); // intentionally (c, r)
                field.setOnMouseClicked(evt -> {
                    if (evt.getButton() == MouseButton.PRIMARY) {
                        if (gc.getCurPlayer() instanceof LocalPlayer) {
                            localPlayerMove(board, fieldPos, fig);
                        }
                        if (gc.getCurPlayer() instanceof AIPlayer) { // check if an AIPlayer does return a random move, not a real implementation
                            AIPlayerMove(board);
                        }
                    }
                });
            }
        }
        return grid;
    }

    private void AIPlayerMove(Board board) {
        gc.getCurPlayer().makeMove(board);
        gc.switchCurPlayer();
        GridPane updatedBoard = drawBoard(board);
        redrawBoard(updatedBoard);
    }

    private void localPlayerMove(Board board, Field fieldPos, Figure fig) {
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
            } else {
                return;
            }
            updatedBoard = drawBoard(board);
        }
        redrawBoard(updatedBoard);
    }

    private BorderPane newClockBox(Player player) {
        BorderPane clockBox = new BorderPane();
        Label playerName = style.newLabel("PlayerName", 20);
        Label playerTime = style.newLabel(player.getTimeString(), 30);
        clockBox.setTop(playerName);
        clockBox.setCenter(playerTime);
        clockBox.setBorder(new Border((new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
        clockBox.setPadding(new Insets(10));
        return clockBox;
    }

    private VBox newOptions() {
        VBox options = new VBox();
        Button menu = style.newButton("Menu");
        menu.setOnAction(sceneController::switchToMenu);
        Button restart = style.newButton("Restart");
        restart.setOnAction(evt -> sceneController.switchToGame(evt, white, black));
        Button save = style.newButton("Save");
        Button load = style.newButton("Load");
        Button create = style.newButton("Create");

        create.setOnAction(evt -> sceneController.switchToGame(evt, white, black, true));

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
            if (applyButton.isPressed()) pawn.promotion(new Queen(pawn.getColor(), board));
        });
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
