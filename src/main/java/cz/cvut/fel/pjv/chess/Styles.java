package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.util.Locale;

public class Styles {

    public final String BROWN = "-fx-background-color: #b5886b";
    public final String WHITE = "-fx-background-color: #f0dec6";
    public final String BROWN_GREEN = "-fx-background-color: #4bb35c";
    public final String WHITE_GREEN = "-fx-background-color: #63d678";
    public final String BROWN_YELLOW = "-fx-background-color: #db9722";
    public final String WHITE_YELLOW = "-fx-background-color: #edb13d";

    public final String font = "Segoe UI";
    public final double size = 70;
    public final double minWidth = 800, minHeight = 600;

    public Label newLabel(String s, int fontSize) {
        Label label = new Label(s);
        label.setFont(new Font(font, fontSize));
        return label;
    }

    public Button newButton(String label) {
        Button button = new Button(label);
        button.setMinWidth(130);
        button.setMinHeight(55);
        button.setFont(new Font(font, 20));
        button.setAlignment(Pos.BASELINE_LEFT);
        button.setStyle("-fx-background-color: #e8e8e8");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #e1e1e1"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #e8e8e8"));
        return button;
    }

    public ImageView getImageFigure(Figure fig) {
        if (fig == null) return null;
        ImageView figImage = new ImageView(new Image(getImagePath(fig)));
        figImage.setFitWidth(size);
        figImage.setFitHeight(size);
        return figImage;
    }

    public String getImagePath(Figure fig) {
        String type = fig.getClass().getSimpleName();
        String name = fig.getColor() == MyColor.WHITE ? "/white_" : "/black_";
        name += type.toLowerCase(Locale.ROOT) + ".png";
        return name;
    }

    public Label newRowLabel(int i) {
        Label l = newLabel(8 - i + "", 15);
        l.setMinSize(20, size);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    public Label newColLabel(int i) {
        Label l = newLabel((char) (i + 65) + "", 15);
        l.setMinSize(size, 20);
        l.setAlignment(Pos.CENTER);
        return l;
    }
}
