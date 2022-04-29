package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;
import cz.cvut.fel.pjv.chess.players.LocalPlayer;
import cz.cvut.fel.pjv.chess.players.Player;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class MainFrame extends Application {

    private final static Background BROWN = new Background(new BackgroundFill(Color.rgb(181, 136, 107), CornerRadii.EMPTY, Insets.EMPTY));
    private final static Background WHITE = new Background(new BackgroundFill(Color.rgb(240, 222, 198), CornerRadii.EMPTY, Insets.EMPTY));
    private final static Background BROWN_GREEN = new Background(new BackgroundFill(Color.rgb(75, 179, 92), CornerRadii.EMPTY, Insets.EMPTY));
    private final static Background WHITE_GREEN = new Background(new BackgroundFill(Color.rgb(99, 214, 120), CornerRadii.EMPTY, Insets.EMPTY));
    private final static Background BROWN_YELLOW = new Background(new BackgroundFill(Color.rgb(219, 151, 34), CornerRadii.EMPTY, Insets.EMPTY));
    private final static Background WHITE_YELLOW = new Background(new BackgroundFill(Color.rgb(237, 177, 61), CornerRadii.EMPTY, Insets.EMPTY));

    private final static String font = "Segoe UI";
    private final static double size = 70;
    private final static double minWidth = 800, minHeight = 600;
    private Stage stage;
    private Scene scene;

    private GridPane boardTable;
    private Figure figureBeingMoved;
    private BorderPane timeWhite;
    private BorderPane timeBlack;
    private Player white;
    private Player black;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a scene and place it in the stage
        scene = new Scene(createMenuScene());

        primaryStage.getIcons().add(new Image("/logo.png"));
        primaryStage.setMinWidth(minWidth);
        primaryStage.setMinHeight(minHeight + 40);
        primaryStage.setTitle("Chess");
        primaryStage.setScene(scene); // Place in scene in the stage
        primaryStage.show();
    }

    public GridPane createMenuScene() {
        Button play = newButton("Player vs AI");
        play.setOnAction(this::switchToGame);

        GridPane root = new GridPane();
        root.setAlignment(Pos.BOTTOM_LEFT);
        root.add(play, 0, 0, 1, 1);
        root.add(newButton("Multiplayer"), 0, 1, 1, 1);
        root.add(newButton("Quit"), 0, 2, 1, 1);
        root.setVgap(20);
        root.setPadding(new Insets(20));
        root.setMinSize(minWidth, minHeight);
        return root;
    }

    public GridPane createGameScene() {
        Board test = new Board();
        test.initialPosition();

        white = new LocalPlayer(MyColor.WHITE);
        black = new LocalPlayer(MyColor.BLACK);
        white.setCurrentPlayer(true);
        black.setCurrentPlayer(false);
        white.setTimeLeft(25*60);
        black.setTimeLeft(25*60);

        GridPane table = new GridPane();
        for (int i = 0; i < 8; i++) {
            table.add(newRowLabel(i), 0, i + 1, 1, 1);
            table.add(newRowLabel(i), 9, i + 1, 1, 1);
            table.add(newColLabel(i), i + 1, 0, 1, 1);
            table.add(newColLabel(i), i + 1, 9, 1, 1);
        }
        boardTable = drawBoard(test);
        table.add(boardTable, 1, 1, 8, 8);
        table.setAlignment(Pos.CENTER);

        // vert menu
        VBox leftVertMenu = new VBox();
        VBox options = newOptions();
        leftVertMenu.setPadding(new Insets(15));
        timeWhite = newClockBox(white);
        timeBlack = newClockBox(black);
        leftVertMenu.getChildren().addAll(timeBlack, options, timeWhite);
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.add(table, 0, 0, 1, 1);
        root.add(leftVertMenu, 1, 0, 1, 1);
        root.setMinSize(minWidth, minHeight);
        return root;
    }

    private GridPane drawBoard(Board board) {
        return drawBoard(board, new HashSet<>());
    }

    private GridPane drawBoard(Board board, Set<Field> validMoves) {
        GridPane grid = new GridPane();
        ColumnConstraints columnConstraints = new ColumnConstraints(size);
        RowConstraints rowConstraints = new RowConstraints(size);

        Field figPos = figureBeingMoved != null ? figureBeingMoved.getPosition() : null;
        for (int r = 0; r <= Board.MAX_ROW; r++) {
            grid.getColumnConstraints().add(columnConstraints);
            grid.getRowConstraints().add(rowConstraints);
            for (int c = 0; c <= Board.MAX_COL; c++) {
                final Field fieldPos = new Field(r, c);
                StackPane field = new StackPane();
                Figure fig = board.getFigure(fieldPos);

                Background white = validMoves.contains(fieldPos) || Objects.equals(fieldPos, figPos) ? WHITE_GREEN : WHITE;
                Background brown = validMoves.contains(fieldPos) || Objects.equals(fieldPos, figPos) ? BROWN_GREEN : BROWN;
                if (fig != null && figureBeingMoved == null && isCurrentColor(fig.getColor())) {
                    white = fig.hasValidMoves() ? WHITE_YELLOW : WHITE;
                    brown = fig.hasValidMoves() ? BROWN_YELLOW : BROWN;
                }

                field.setBackground(((r + c) & 1) == 0 ? white : brown);
                ImageView image = getImageFigure(fig);
                if (image != null) {
                    field.getChildren().add(image);
                }
                grid.add(field, c, r); // intentionally (c, r)

                field.setOnMouseClicked(evt -> {
                    GridPane updatedBoard;
                    if (figureBeingMoved == null) {
                        if (fig == null || !isCurrentColor(fig.getColor()) || !fig.hasValidMoves()) return;
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
                            switchCurPlayer();
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

    private void redrawBoard(GridPane newBoardTable) {
        GridPane parentTable = (GridPane) boardTable.getParent();
        ((GridPane) boardTable.getParent()).getChildren().remove(boardTable);
        parentTable.add(newBoardTable, 1, 1, 8, 8);
        boardTable = newBoardTable;
    }

    private ImageView getImageFigure(Figure fig) {
        if (fig == null) return null;
        ImageView figImage = new ImageView(new Image(getImagePath(fig)));
        figImage.setFitWidth(size);
        figImage.setFitHeight(size);
        return figImage;
    }

    private String getImagePath(Figure fig) {
        String type = fig.getClass().getSimpleName();
        String name = fig.getColor() == MyColor.WHITE ? "/white_" : "/black_";
        name += type.toLowerCase(Locale.ROOT) + ".png";
        return name;
    }

    private Label newRowLabel(int i) {
        Label l = newLabel(8 - i + "", 15);
        l.setMinSize(20, size);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    private Label newColLabel(int i) {
        Label l = newLabel((char) (i + 65) + "", 15);
        l.setMinSize(size, 20);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    private Button newButton(String label) {
        Button button = new Button(label);
        button.setFont(new Font(font, 20));
        button.setPrefSize(150, 60);
        button.setAlignment(Pos.BASELINE_LEFT);
        button.setStyle("-fx-background-color: #e8e8e8");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #e1e1e1"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #e8e8e8"));
        return button;
    }

    private BorderPane newClockBox(Player player) {
        BorderPane clockBox = new BorderPane();
        Label playerName = newLabel("PlayerName", 20);
        Label playerTime = newLabel(player.getTimeString(), 30);
        clockBox.setTop(playerName);
        clockBox.setCenter(playerTime);
        clockBox.setBorder(new Border((new BorderStroke(Color.BLACK,
            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
        clockBox.setPadding(new Insets(10));
        return clockBox;
    }

    private VBox newOptions() {
        VBox options = new VBox();
        Button restart = newButton("Restart");
        restart.setOnAction(this::switchToGame);
        Button menu = newButton("Menu");
        menu.setOnAction(this::switchToMenu);
        Button save = newButton("Save");
        Button load = newButton("Load");
        Button create = newButton("Create");
        options.setSpacing(10);
        options.setPadding(new Insets(25, 0, 25, 0));
        options.getChildren().addAll(restart, menu, save, load, create);
        return options;
    }

    private Label newLabel(String s, int fontSize) {
        Label label = new Label(s);
        label.setFont(new Font(font, fontSize));
        return label;
    }

    public void switchToGame(ActionEvent event) {
        figureBeingMoved = null;
        GridPane gameScene = createGameScene();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(gameScene);
        stage.setScene(scene);
        stage.show();
        Clock clock = new Clock(white, black, timeWhite, timeBlack);
        Thread timer = new Thread(clock);
        timer.start();
    }

    public void switchToMenu(ActionEvent event) {
        GridPane menuScene = createMenuScene();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(menuScene);
        stage.setScene(scene);
        stage.show();
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

        dialog.setOnCloseRequest(evt -> pawn.promotion(new Queen(pawn.getColor(), board)));
        dialog.getDialogPane().setContent(vbox);
        dialog.showAndWait();
        dialog.close();
    }

    public Button newPromotionButton(Figure figure, Pawn pawn, Button applyButton) {
        Button button = newButton(figure.getClass().getSimpleName());
        button.setOnAction(e -> {
            pawn.promotion(figure);
            applyButton.fire();
        });
        return button;
    }

    private Player getCurPlayer() {
        return white.isCurrentPlayer() ? white : black;
    }

    private void switchCurPlayer() {
        if (white.isCurrentPlayer()) {
            white.setCurrentPlayer(false);
            black.setCurrentPlayer(true);
        } else {
            black.setCurrentPlayer(false);
            white.setCurrentPlayer(true);
        }
    }

    private boolean isCurrentColor(MyColor color) {
        return white.isCurrentPlayer() ? color == MyColor.WHITE : color == MyColor.BLACK;
    }
}
