package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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



        GridPane table = new GridPane();
        for (int i = 0; i < 8; i++) {
            table.add(newRowLabel(i), 0, i + 1, 1, 1);
            table.add(newRowLabel(i), 9, i + 1, 1, 1);
            table.add(newColLabel(i), i + 1, 0, 1, 1);
            table.add(newColLabel(i), i + 1, 9, 1, 1);
        }
        table.add(drawBoard(test), 1, 1, 8, 8);
        table.setAlignment(Pos.CENTER);

        // Create a scene and place it in the stage
        Scene scene = new Scene(table);
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
        for (int i = 0; i < 8; i++) {
            grid.getColumnConstraints().add(columnConstraints);
            grid.getRowConstraints().add(rowConstraints);
            for (int j = 0; j < 8; j++) {
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

    private String getImagePath(Figure fig){
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
}
