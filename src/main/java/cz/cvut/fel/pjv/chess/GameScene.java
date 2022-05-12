package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;
import cz.cvut.fel.pjv.chess.players.AIPlayer;
import cz.cvut.fel.pjv.chess.players.LocalPlayer;
import cz.cvut.fel.pjv.chess.players.Player;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GameScene extends GridPane {

    private final Styles style = new Styles();
    private final SceneController sceneController = new SceneController();
    private final Player white;
    private final Player black;
    private final GameController gc;
    Clock clock;
    private Board board;
    private GridPane boardTable;
    private Figure figureBeingMoved;
    private boolean createMode = false;

    public GameScene(Player white, Player black, Board board) {
        this.white = white;
        this.black = black;
        this.board = board;
        this.gc = new GameController(white, black);
    }

    public GameScene(Player white, Player black, boolean createMode) {
        this.white = white;
        this.black = black;
        this.createMode = createMode;
        this.gc = new GameController(white, black);
    }

    public GridPane createGameScene() {
        if (board == null) {
            board = new Board();
            board.initialPosition();
        }
        figureBeingMoved = null;

        white.setCurrentPlayer(true);
        black.setCurrentPlayer(false);
        white.setTimeLeft(25 * 60 * 1000);
        black.setTimeLeft(25 * 60 * 1000);

        GridPane table = new GridPane();
        for (int i = 0; i < 8; i++) {
            table.add(style.newRowLabel(i), 0, i + 1, 1, 1);
            table.add(style.newRowLabel(i), 9, i + 1, 1, 1);
            table.add(style.newColLabel(i), i + 1, 0, 1, 1);
            table.add(style.newColLabel(i), i + 1, 9, 1, 1);
        }
        boardTable = drawBoard(board);
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

        if (clock != null) {
            clock.shutdown();
        }
        clock = new Clock(white, black, timeWhite, timeBlack);
        Thread timer = new Thread(clock);
        timer.setPriority(Thread.MAX_PRIORITY);
        timer.setDaemon(true);
        if (!createMode) {
            timer.start();
        }

        return root;
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

                String white;
                String brown;
                if (!createMode) {
                    white = validMoves.contains(fieldPos) || Objects.equals(fieldPos, figPos) ? style.WHITE_GREEN : style.WHITE;
                    brown = validMoves.contains(fieldPos) || Objects.equals(fieldPos, figPos) ? style.BROWN_GREEN : style.BROWN;
                    if (fig != null && figureBeingMoved == null && gc.isCurrentColor(fig.getColor())) {
                        white = fig.hasValidMoves() ? style.WHITE_YELLOW : style.WHITE;
                        brown = fig.hasValidMoves() ? style.BROWN_YELLOW : style.BROWN;
                    }
                } else {
                    white = Objects.equals(fieldPos, figPos) ? style.WHITE_GREEN : style.WHITE;
                    brown = Objects.equals(fieldPos, figPos) ? style.BROWN_GREEN : style.BROWN;
                }
                field.setStyle(((r + c) & 1) == 0 ? white : brown);

                ImageView image = style.getImageFigure(fig);
                if (image != null) {
                    field.setGraphic(image);
                }

                if (createMode) {
                    ContextMenu cm = new CreateMenu(board, fieldPos, field);
                    field.setContextMenu(cm);
                    field.setOnContextMenuRequested(e -> cm.show(field, e.getScreenX(), e.getScreenY()));
                }

                grid.add(field, c, r); // intentionally (c, r)

                field.setOnMouseClicked(evt -> {
                    if (evt.getButton() == MouseButton.PRIMARY) {
                        if (!createMode) {
                            if (gc.getCurPlayer() instanceof LocalPlayer && gc.isCurrentColor(gc.getCurPlayer().getColor())) {
                                System.out.println("Local");
                                localPlayerMove(board, fieldPos, fig);
                            }
                            if (gc.getCurPlayer() instanceof AIPlayer) { // check if an AIPlayer does return a random move, not a real implementation
                                System.out.println("AI");
                                AIPlayerMove(board);
                            }
                        } else {
                            if (fig != null && fig == figureBeingMoved) return;
                            if (figureBeingMoved == null) {
                                if (fig == null) return;
                                figureBeingMoved = fig;
                            } else {
                                board.moveFigure(figureBeingMoved, fieldPos);
                                if (figureBeingMoved instanceof Pawn && ((Pawn) figureBeingMoved).moveLeadsToPromotion(fieldPos)) {
                                    promotionDialog((Pawn) figureBeingMoved, board);
                                }
                                figureBeingMoved = null;
                            }
                            redrawBoard(drawBoard(board));
                        }
                    }
                });
            }
        }
        return grid;
    }

    private void AIPlayerMove(Board board) {
        new Thread(() -> {
            gc.getCurPlayer().makeMove(board);
            gc.switchCurPlayer();
            Platform.runLater(() -> {
                GridPane updatedBoard = drawBoard(board);
                redrawBoard(updatedBoard);
            });
        }).start();
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
        Button restart;
        if (!createMode) {
            restart = style.newButton("Restart");
            restart.setOnAction(evt -> {
                stopClock();
                sceneController.switchToGame(evt, white, black);
            });
        } else {
            restart = style.newButton("Start");
            restart.setStyle("-fx-background-color: #e8b5b5");
            restart.setOnAction(evt -> {
                stopClock();
                sceneController.switchToGame(evt, white, black, board);
            });
        }
        Button save = style.newButton("Save");
        Button load = style.newButton("Load");
        Button create = style.newButton("Create");
        create.setOnAction(evt -> {
            stopClock();
            sceneController.switchToGame(evt, white, black, true);
        });
        options.setSpacing(15);
        options.setPadding(new Insets(25, 0, 25, 0));
        options.getChildren().addAll(menu, restart, save, load, create);
        return options;
    }

    private void stopClock() {
        if (clock != null) {
            clock.shutdown();
        }
    }

    private void redrawBoard(GridPane newBoardTable) {
        GridPane parentTable = (GridPane) boardTable.getParent();
        ((GridPane) boardTable.getParent()).getChildren().remove(boardTable);
        parentTable.add(newBoardTable, 1, 1, 8, 8);
        boardTable = newBoardTable;
    }

    public void promotionDialog(Pawn pawn, Board board) {
        Dialog<Class<? extends Figure>> dialog = new Dialog<>();
        dialog.setTitle("Promotion");
        dialog.setContentText("Promote the pawn to:");

        // Allow closing the dialog (https://stackoverflow.com/a/32058500)
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);

        final List<Class<? extends Figure>> possiblePromFigClasses = Arrays.asList(Queen.class, Knight.class, Rook.class, Bishop.class);
        final Map<ButtonType, Class<? extends Figure>> figClassByBtnType = new HashMap<>();
        for (Class<? extends Figure> figClass : possiblePromFigClasses) {
            ButtonType btnType = new ButtonType(figClass.getSimpleName());
            dialog.getDialogPane().getButtonTypes().add(btnType);
            figClassByBtnType.put(btnType, figClass);
        }
        dialog.setResultConverter(figClassByBtnType::get);

        Optional<Class<? extends Figure>> result = dialog.showAndWait();
        try {
            pawn.promotion(result
                .orElse(Queen.class)
                .getConstructor(MyColor.class, Board.class)
                .newInstance(pawn.getColor(), board));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
