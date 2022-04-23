package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;
import cz.cvut.fel.pjv.chess.players.Player;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Locale;

public class MainFrame extends Application {
    final double size = 70;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // test board for the sake of testing
        Board test = new Board();
        Rook oppRook = new Rook(MyColor.BLACK, test);
        test.moveFigure(oppRook, new Field(1, 4));
        Bishop oppBishop = new Bishop(MyColor.BLACK, test);
        test.moveFigure(oppBishop, new Field(2, 1));
        Pawn myPawn = new Pawn(MyColor.WHITE, test);
        test.moveFigure(myPawn, new Field(4, 3));
        King figure = new King(MyColor.WHITE, test);
        test.moveFigure(figure, new Field(5, 4));
        King figure1 = new King(MyColor.BLACK, test);
        test.moveFigure(figure1, new Field(7, 4));

        BorderPane pane = new BorderPane();
        primaryStage.getIcons().add(new Image("/logo.png"));

        GridPane table = new GridPane();
        for (int i = 0; i < 8; i++) {
            table.add(newRowLabel(i), 0, i + 1, 1, 1);
            table.add(newRowLabel(i), 9, i + 1, 1, 1);
            table.add(newColLabel(i), i + 1, 0, 1, 1);
            table.add(newColLabel(i), i + 1, 9, 1, 1);
        }
        table.add(drawBoard(test), 1, 1, 8, 8);
        table.setAlignment(Pos.CENTER);

        pane.setLeft(table);

        // vert menu
        BorderPane leftVertMenu = new BorderPane();
        leftVertMenu.setPadding(new Insets(0, 20, 0, 20));
        GridPane elements = new GridPane();
        elements.setAlignment(Pos.CENTER);

        elements.add(newClockBox(),0,0,1,1);
        elements.add(newButton("Restart"), 0, 1, 1, 1);
        elements.add(newButton("Menu"), 0, 2, 1, 1);
        elements.add(newButton("Save"), 0, 3, 1, 1);
        elements.add(newButton("Load"), 0, 4, 1, 1);
        elements.add(newButton("Create"), 0, 5, 1, 1);
        elements.add(newClockBox(),0,6,1,1);

        leftVertMenu.setRight(elements);
        pane.setRight(leftVertMenu);

        // Create a scene and place it in the stage
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Chess");
        primaryStage.setScene(scene); // Place in scene in the stage
        primaryStage.show();


    }

    private GridPane drawBoard(Board board) {
        final Background brown = new Background(new BackgroundFill(Color.rgb(181, 136, 107), CornerRadii.EMPTY, Insets.EMPTY));
        final Background white = new Background(new BackgroundFill(Color.rgb(240, 222, 198), CornerRadii.EMPTY, Insets.EMPTY));

        GridPane grid = new GridPane();
        ColumnConstraints columnConstraints = new ColumnConstraints(size);
        RowConstraints rowConstraints = new RowConstraints(size);
        for (int i = 0; i <= Board.MAX_ROW; i++) {
            grid.getColumnConstraints().add(columnConstraints);
            grid.getRowConstraints().add(rowConstraints);
            for (int j = 0; j <= Board.MAX_COL; j++) {
                StackPane field = new StackPane();
                field.setBackground(((i + j) & 1) == 0 ? white : brown);
                ImageView image = getImageFigure(board, i, j);
                if (image != null) {
                    field.getChildren().add(image);
                }
                grid.add(field, i, j);
            }
        }
        return grid;
    }

    private ImageView getImageFigure(Board board, int row, int col) {
        Field pos = new Field(row, col);
        Figure fig = board.getFigure(pos);
        if (fig == null) {
            return null;
        }
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
        Label l = new Label(8 - i + "");
        l.setFont(new Font("Segoe UI", 15));
        l.setMinSize(20, size);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    private Label newColLabel(int i) {
        Label l = new Label((char) (i + 65) + "");
        l.setFont(new Font("Segoe UI", 15));
        l.setMinSize(size, 20);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    private Button newButton(String label) {
        Button button = new Button(label);
        button.setFont(new Font("Segoe UI", 24));
        button.setPrefSize(120, 60);
        button.setAlignment(Pos.BASELINE_LEFT);
//        button.setStyle("-fx-background-color: #e8e8e8");
        return button;
    }

    private StackPane newClockBox(){
        StackPane clockBox = new StackPane();
        Label playerName = new Label("PlayerName");
        Label playerTime = new Label("25:00");
        clockBox.getChildren().add(playerName);
        clockBox.getChildren().add(playerTime);
        return clockBox;
    }
}
