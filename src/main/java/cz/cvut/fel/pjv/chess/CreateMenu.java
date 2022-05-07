package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

public class CreateMenu extends ContextMenu {
    private final Styles style = new Styles();
    private final Board board;

    public CreateMenu(Board board, Field toPos, Label field) {
        this.board = board;
        getItems().add(new CreateMenuOption("Remove Figure", e -> setFigure(null, toPos, field)));
        getItems().add(new CreateMenuOption("Add White Queen", e -> setFigure(new Queen(MyColor.WHITE, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add White Bishop", e -> setFigure(new Bishop(MyColor.WHITE, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add White Knight", e -> setFigure(new Knight(MyColor.WHITE, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add White Rook", e -> setFigure(new Rook(MyColor.WHITE, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add White King", e -> setFigure(new King(MyColor.WHITE, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add White Pawn", e -> setFigure(new Pawn(MyColor.WHITE, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add Black Queen", e -> setFigure(new Queen(MyColor.BLACK, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add Black Bishop", e -> setFigure(new Bishop(MyColor.BLACK, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add Black Knight", e -> setFigure(new Knight(MyColor.BLACK, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add Black Rook", e -> setFigure(new Rook(MyColor.BLACK, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add Black King", e -> setFigure(new King(MyColor.BLACK, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add Black Pawn", e -> setFigure(new Pawn(MyColor.BLACK, board), toPos, field)));
    }

    private void setFigure(Figure figure, Field toPos, Label field) {
        board.setFigure(toPos, figure);
        field.setGraphic(style.getImageFigure(figure));
    }

    private static class CreateMenuOption extends MenuItem {
        CreateMenuOption(String text, EventHandler<ActionEvent> event) {
            setText(text);
            setOnAction(event);
        }
    }
}
