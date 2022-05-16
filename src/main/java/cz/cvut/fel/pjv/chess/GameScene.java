package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;
import cz.cvut.fel.pjv.chess.players.AIPlayer;
import cz.cvut.fel.pjv.chess.players.LocalPlayer;
import cz.cvut.fel.pjv.chess.players.Player;
import cz.cvut.fel.pjv.chess.players.RemotePlayer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class GameScene extends GridPane {

    private final Styles style = new Styles();
    private final SceneController sceneController = new SceneController();
    private final Player white;
    private final Player black;
    private final GameMode gameMode;
    Clock clock;
    private GameController gc;
    private Board board;
    private GridPane boardTable;
    private Figure figureBeingMoved;

    public GameScene(Player white, Player black, GameMode gameMode, Board board) {
        this.white = white;
        this.black = black;
        this.gameMode = gameMode;
        this.board = board;

        setUpRemotePlayer();
    }

    public GameScene(Player white, Player black, GameMode gameMode) {
        this.white = white;
        this.black = black;
        this.gameMode = gameMode;
        setUpRemotePlayer();
    }

    private void setUpRemotePlayer() {
        RemotePlayer p = getRemotePlayer();
        if (p == null) return;
        p.setMoveCallback(this::remotePlayerMoveReceived);
        p.setOpponentSurrenderCallback(this::opponentSurrenderAlert);
        p.setDrawOfferDialogCallback(this::drawOfferDialog);
        p.setGameDrawCallback(this::gameDraw);
    }

    private RemotePlayer getRemotePlayer() {
        RemotePlayer p = null;
        if (white instanceof RemotePlayer) {
            p = (RemotePlayer) white;
        } else if (black instanceof RemotePlayer) {
            p = (RemotePlayer) black;
        }
        return p;
    }

    public GridPane createGameScene(Window window) {
        if (board == null) {
            board = new Board();
            board.initialPosition();
        }
        if (gameMode != GameMode.CREATE) {
            board.switchToGameMode();
        }

        gc = new GameController(white, black, this, board);
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
        VBox options = createOptions(window);
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
        if (gameMode != GameMode.CREATE) {
            timer.start();
        }
        gc.start();

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

                if (gameMode == GameMode.CREATE) {
                    ContextMenu cm = new CreateMenu(board, fieldPos, field);
                    if (figureBeingMoved == null) {
                        field.setContextMenu(cm);
                        field.setOnContextMenuRequested(e -> cm.show(field, e.getScreenX(), e.getScreenY()));
                    }
                }

                field.setMinSize(style.size, style.size);
                field.setMaxSize(style.size, style.size);
                {
                    final Figure fig = board.getFigure(fieldPos);
                    String white;
                    String brown;
                    if (gameMode != GameMode.CREATE) {
                        white = validMoves.contains(fieldPos) || Objects.equals(fieldPos, figPos) ? style.WHITE_GREEN : style.WHITE;
                        brown = validMoves.contains(fieldPos) || Objects.equals(fieldPos, figPos) ? style.BROWN_GREEN : style.BROWN;
                        if (fig != null && figureBeingMoved == null && gc.isCurrentColor(fig.getColor()) && gc.getCurPlayer() instanceof LocalPlayer) {
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
                    grid.add(field, c, r); // intentionally (c, r)
                }

                field.setOnMouseClicked(evt -> {
                    if (evt.getButton() != MouseButton.PRIMARY) return;
                    Figure fig = board.getFigure(fieldPos);
                    if (gameMode != GameMode.CREATE) {
                        if (gc.getCurPlayer() instanceof LocalPlayer && gc.isCurrentColor(gc.getCurPlayer().getColor())) {
                            localPlayerMove(board, fieldPos, fig);
                        }
                        if (gc.getCurPlayer() instanceof AIPlayer) { // check if an AIPlayer does return a random move, not a real implementation
                            AIPlayerMove(board);
                        }
                    } else {
                        createModeMove(fig, fieldPos);
                    }
                });
            }
        }
        return grid;
    }

    private void createModeMove(Figure fig, Field fieldPos){
        if (fig != null && fig == figureBeingMoved) {
            return;
        }
        if (figureBeingMoved == null) {
            if (fig == null) {
                return;
            }
            figureBeingMoved = fig;
        } else {
            if (board.getFigure(fieldPos) instanceof King) return;
            board.moveFigure(figureBeingMoved, fieldPos);
            if (figureBeingMoved instanceof Pawn && ((Pawn) figureBeingMoved).moveLeadsToPromotion(fieldPos)) {
                promotionDialog((Pawn) figureBeingMoved);
            }
            figureBeingMoved = null;
        }
        redrawBoard(drawBoard(board));
    }

    public void remotePlayerMoveReceived() {
        gc.switchCurPlayer();
        GridPane updatedBoard = drawBoard(board);
        redrawBoard(updatedBoard);
    }

    public void opponentSurrenderAlert() {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            getRemotePlayer().setLost(true);
            gc.getOpponent(getRemotePlayer()).setWon(true);
            updateClock();
            a.setTitle(" ");
            a.setHeaderText("You win.");
            a.setContentText("You opponent has surrendered.");
            a.show();
            stopClock();
        });
    }

    public void drawOfferDialog() {
        Platform.runLater(() -> {
            RemotePlayer player = getRemotePlayer();
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Draw Offer");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
            Button applyButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.APPLY);
            applyButton.setVisible(false);

            VBox vbox = new VBox();
            vbox.getChildren().add(newDrawResponseButton("Yes", applyButton, player));
            vbox.getChildren().add(newDrawResponseButton("No", applyButton, player));

            dialog.setOnCloseRequest(evt -> dialog.close());
            dialog.getDialogPane().setContent(vbox);
            dialog.showAndWait();
            dialog.close();
        });
    }

    private Button newDrawResponseButton(String response, Button applyButton, RemotePlayer player) {
        Button button = style.newButton(response);
        button.setOnAction(e -> {
            player.sendDrawResponse(response);
            if (response.equals("Yes")) {
                white.setDraw(true);
                black.setDraw(true);
                updateClock();
                stopClock();
            }
            applyButton.fire();
        });
        return button;
    }

    public void AIPlayerMove(Board board) {
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
                Field from = figureBeingMoved.getPosition();
                board.moveFigure(figureBeingMoved, fieldPos);
                Class<? extends Figure> promClass = null;
                if (figureBeingMoved instanceof Pawn && ((Pawn) figureBeingMoved).moveLeadsToPromotion(fieldPos)) {
                    promClass = promotionDialog((Pawn) figureBeingMoved);
                }
                if (gc.getOpponent(gc.getCurPlayer()) instanceof RemotePlayer) {
                    ((RemotePlayer) gc.getOpponent(gc.getCurPlayer())).sendMove(from, fieldPos, promClass);
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
        String name = player.getName();
        if (name == null) {
            name = player.getColor() == MyColor.WHITE ? "White" : "Black";
        }
        Label playerName = style.newLabel(name, 20);
        Label playerTime = style.newLabel(player.getTimeString(), 30);
        clockBox.setTop(playerName);
        clockBox.setCenter(playerTime);
        clockBox.setBorder(new Border((new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
        clockBox.setPadding(new Insets(10));
        return clockBox;
    }

    private void cannotStartAlert(){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(" ");
        a.setHeaderText("Cannot start game.");
        a.setContentText("King must not be in check.");
        a.show();
    }

    private VBox createOptions(Window window) {
        VBox options = new VBox();
        Button menu = style.newButton("Menu");
        menu.setOnAction(evt->{
            if (gameMode == GameMode.ONLINE){
                getRemotePlayer().sendSurrender();
            }
            sceneController.switchToMenu(evt);
        });
        Button restart;
        if (gameMode == GameMode.CREATE) {
            restart = style.newButton("Start");
            restart.setStyle("-fx-background-color: #63d678");
            restart.setOnAction(evt -> {
                stopClock();
                resetPlayers();
                if (board.getKing(MyColor.BLACK).isInCheck()){
                    cannotStartAlert();
                } else {
                    sceneController.switchToGame(evt, white, black, board);
                }
            });
        } else if (gameMode == GameMode.ONLINE) {
            restart = style.newButton("Draw offer");
            restart.setOnAction(evt -> {
                getRemotePlayer().sendDrawOffer();
                updateClock();
            });
        } else {
            restart = style.newButton("Restart");
            restart.setOnAction(evt -> {
                stopClock();
                resetPlayers();
                sceneController.switchToGame(evt, white, black);
            });
        }
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("game.pgn");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Portable Game Notation file", "*.pgn"));
        Button save = style.newButton("Save");
        final SimpleDateFormat pgnDateFormatter = new SimpleDateFormat("yyyy.MM.dd");
        save.setOnAction(evt -> {
            File file = fileChooser.showSaveDialog(window);
            if (file == null) {
                return;
            }
            try (
                PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8)
            ) {
                PGN pgn = new PGN();
                final String modeName = switch (gameMode) {
                    case CREATE -> "Create";
                    case ONLINE -> "Online";
                    case LOCAL -> "Local";
                };
                pgn.setTagValue("Event",
                    (gameMode == GameMode.CREATE ? "Custom board setup created in " : "Game in ")
                        + modeName + " mode");
                pgn.setTagValue("Site", "Chess implementation by rystidia and pucilpet for B0B36PJV");
                pgn.setTagValue("Date", pgnDateFormatter.format(new Date()));
                pgn.setTagValue("Round", "-");
                pgn.setTagValue("White", white.getName() != null ? white.getName() : "?");
                pgn.setTagValue("Black", black.getName() != null ? black.getName() : "?");
                // https://github.com/mliebelt/pgn-spec-commented/blob/0c532f7/pgn-spec-supplement.md#6-clock-start-time
                pgn.setTagValue("WhiteClock", white.getTimeString(true));
                pgn.setTagValue("BlackClock", black.getTimeString(true));
                pgn.save(writer, board);
            } catch (IOException e) {
                Alert a = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                a.show();
            }
        });
        Button load = style.newButton("Load");
        Button create;
        if (gameMode != GameMode.ONLINE) {
            create = style.newButton("Create");
            create.setOnAction(evt -> {
                stopClock();
                sceneController.switchToGame(evt, white, black, GameMode.CREATE);
            });
        } else {
            create = style.newButton("Surrender");
            create.setOnAction(evt -> {
                getRemotePlayer().setWon(true);
                gc.getOpponent(getRemotePlayer()).setLost(true);
                updateClock();
                stopClock();
                getRemotePlayer().sendSurrender();
            });
        }
        options.setSpacing(15);
        options.setPadding(new Insets(25, 0, 25, 0));
        options.getChildren().addAll(menu, restart, save, load, create);
        return options;
    }

    private void gameDraw(){
        white.setDraw(true);
        black.setDraw(true);
        updateClock();
        stopClock();
    }

    private void resetPlayers() {
        white.setWon(false);
        white.setLost(false);
        white.setDraw(false);
        black.setWon(false);
        black.setLost(false);
        black.setDraw(false);
    }

    private void updateClock() {
        Platform.runLater(() -> {
            clock.redrawClockBox(white);
            clock.redrawClockBox(black);
        });
    }

    private void stopClock() {
        if (clock != null) {
            clock.shutdown();
        }
    }

    private void redrawBoard(GridPane newBoardTable) {
        Platform.runLater(() -> {
            GridPane parentTable = (GridPane) boardTable.getParent();
            ((GridPane) boardTable.getParent()).getChildren().remove(boardTable);
            parentTable.add(newBoardTable, 1, 1, 8, 8);
            boardTable = newBoardTable;
        });
    }

    public Class<? extends Figure> promotionDialog(Pawn pawn) {
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
        pawn.promotion(result.orElse(Queen.class));
        return result.orElse(Queen.class);
    }
}
