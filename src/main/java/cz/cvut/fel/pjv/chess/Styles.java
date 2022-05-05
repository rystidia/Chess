package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Locale;

public class Styles {
    public final Background BROWN = new Background(new BackgroundFill(Color.rgb(181, 136, 107), CornerRadii.EMPTY, Insets.EMPTY));
    public final Background WHITE = new Background(new BackgroundFill(Color.rgb(240, 222, 198), CornerRadii.EMPTY, Insets.EMPTY));
    public final Background BROWN_GREEN = new Background(new BackgroundFill(Color.rgb(75, 179, 92), CornerRadii.EMPTY, Insets.EMPTY));
    public final Background WHITE_GREEN = new Background(new BackgroundFill(Color.rgb(99, 214, 120), CornerRadii.EMPTY, Insets.EMPTY));
    public final Background BROWN_YELLOW = new Background(new BackgroundFill(Color.rgb(219, 151, 34), CornerRadii.EMPTY, Insets.EMPTY));
    public final Background WHITE_YELLOW = new Background(new BackgroundFill(Color.rgb(237, 177, 61), CornerRadii.EMPTY, Insets.EMPTY));

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
