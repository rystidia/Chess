package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;


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
        getItems().add(new CreateMenuOption("Add White Pawn", e -> setFigure(new Pawn(MyColor.WHITE, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add Black Queen", e -> setFigure(new Queen(MyColor.BLACK, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add Black Bishop", e -> setFigure(new Bishop(MyColor.BLACK, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add Black Knight", e -> setFigure(new Knight(MyColor.BLACK, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add Black Rook", e -> setFigure(new Rook(MyColor.BLACK, board), toPos, field)));
        getItems().add(new CreateMenuOption("Add Black Pawn", e -> setFigure(new Pawn(MyColor.BLACK, board), toPos, field)));
    }

    private void setFigure(Figure figure, Field toPos, Label field) {
        Figure fig = board.getFigure(toPos);
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(" ");
        a.setHeaderText(figure != null ? "Cannot place figure." : "Cannot remove figure");

        if (fig instanceof King) {
            a.setContentText("Each Player must have a King.");
            a.show();
            return;
        } else if (figure != null && board.getNumOfFigs(figure.getColor()) > 15) {
            a.setContentText("The maximum number of figures of the same color is 16.");
            a.show();
            return;
        } else if (figure instanceof Pawn && board.getNumOfSameFigs(figure) > 7) {
            a.setContentText("The maximum number of pawns of the same color is 8.");
            a.show();
            return;
        } else if (figure instanceof Pawn && (toPos.row == 7 || toPos.row == 0)) {
            a.setContentText("A pawn cannot be placed here.");
            a.show();
            return;
        } else if (figure != null && !board.canPlaceFigure(figure)) {
            a.setContentText("The number of promoted pawns must be greater than or equal to the number of additional pieces.");
            a.show();
            return;
        }
        {
            Board newBoard = new Board(board);
            if (figure == null) {
                newBoard.setFigure(toPos, null);
            } else {
                Figure clonedFigure = figure.clone(newBoard);
                newBoard.moveFigure(clonedFigure, toPos);
            }

            if (newBoard.getKing(MyColor.WHITE).isInCheck() || newBoard.getKing(MyColor.BLACK).isInCheck()) {
                a.setContentText("The king must not be in check after placing/removing a piece.");
                a.show();
                return;
            }
        }
        if (figure == null) {
            board.setFigure(toPos, null);
        } else {
            board.moveFigure(figure, toPos);
        }
        field.setGraphic(style.getImageFigure(figure));
    }

    private static class CreateMenuOption extends MenuItem {
        CreateMenuOption(String text, EventHandler<ActionEvent> event) {
            setText(text);
            setOnAction(event);
        }
    }
}
