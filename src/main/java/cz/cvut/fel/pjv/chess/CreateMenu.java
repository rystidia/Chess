package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class CreateMenu extends ContextMenu {
    private final Board board;

    public CreateMenu(Board board, Field field) {
        this.board = board;
        getItems().add(new CreateMenuOption("Remove Figure", e -> setFigure(null, field)));
        getItems().add(new CreateMenuOption("Add White Queen", e -> setFigure(new Queen(MyColor.WHITE, board), field)));
        getItems().add(new CreateMenuOption("Add White Bishop", e -> setFigure(new Bishop(MyColor.WHITE, board), field)));
        getItems().add(new CreateMenuOption("Add White Knight", e -> setFigure(new Knight(MyColor.WHITE, board), field)));
        getItems().add(new CreateMenuOption("Add White Rook", e -> setFigure(new Rook(MyColor.WHITE, board), field)));
        getItems().add(new CreateMenuOption("Add White King", e -> setFigure(new King(MyColor.WHITE, board), field)));
        getItems().add(new CreateMenuOption("Add White Pawn", e -> setFigure(new Pawn(MyColor.WHITE, board), field)));
        getItems().add(new CreateMenuOption("Add Black Queen", e -> setFigure(new Queen(MyColor.BLACK, board), field)));
        getItems().add(new CreateMenuOption("Add Black Bishop", e -> setFigure(new Bishop(MyColor.BLACK, board), field)));
        getItems().add(new CreateMenuOption("Add Black Knight", e -> setFigure(new Knight(MyColor.BLACK, board), field)));
        getItems().add(new CreateMenuOption("Add Black Rook", e -> setFigure(new Rook(MyColor.BLACK, board), field)));
        getItems().add(new CreateMenuOption("Add Black King", e -> setFigure(new King(MyColor.BLACK, board), field)));
        getItems().add(new CreateMenuOption("Add Black Pawn", e -> setFigure(new Pawn(MyColor.BLACK, board), field)));
    }

    private void setFigure(Figure figure, Field field) {
        board.setFigure(field, figure);
    }

    private static class CreateMenuOption extends MenuItem {
        CreateMenuOption(String text, EventHandler<ActionEvent> event) {
            setText(text);
            setOnAction(event);
        }
    }
}
