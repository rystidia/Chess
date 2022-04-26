package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;

import java.util.Arrays;
import java.util.Set;

/**
 * A board for a chess game
 * Board is nothing, but a matrix of the Fields
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public class Board {
    public static final int MAX_ROW = 7;
    public static final int MAX_COL = 7;

    private final Figure[][] board;
    private Figure bKing;
    private Figure wKing;

    /**
     * Initializes the board
     */
    public Board() {
        this.board = new Figure[MAX_ROW + 1][MAX_COL + 1];
    }

    public Figure getFigure(Field pos) {
        return board[pos.row][pos.column];
    }

    public void setFigure(Field pos, Figure figure) {
        board[pos.row][pos.column] = figure;
    }

    public void moveFigure(Figure figure, Field toPos) {
        if (figure.getPosition() != null) {
            if (getFigure(figure.getPosition()) != figure) {
                throw new IllegalArgumentException("provided figure was not found where it should be");
            }
            setFigure(figure.getPosition(), null);
        }
//        if (getFigure(toPos) != null) {
//            throw new UnsupportedOperationException("toPos already occupied, capturing is not implemented yet");
//        }
        figure.move(toPos);
    }

    /**
     * Places figures to the fields
     */
    public void initialPosition() {
        for (MyColor color: Arrays.asList(MyColor.BLACK, MyColor.WHITE)) {
            int row = color == MyColor.WHITE ? 7 : 0;
            moveFigure(new Rook(color, this), new Field(row, 0));
            moveFigure(new Rook(color, this), new Field(row, 7));
            moveFigure(new Knight(color, this), new Field(row, 1));
            moveFigure(new Knight(color, this), new Field(row, 6));
            moveFigure(new Bishop(color, this), new Field(row, 2));
            moveFigure(new Bishop(color, this), new Field(row, 5));
            moveFigure(new Queen(color, this), new Field(row, 3));
            moveFigure(new King(color, this), new Field(row, 4));
        }
        for (int i = 0; i <= MAX_COL; i++) {
            moveFigure(new Pawn(MyColor.WHITE, this), new Field(6, i));
            moveFigure(new Pawn(MyColor.BLACK, this), new Field(1, i));
        }
    }

    /**
     * Returns all valid moves for given figure, checks if king is not in check after them
     */
    public Set<Field> getValidMoves(Figure figure) {
        throw new UnsupportedOperationException();
    }

    public Figure getKing(MyColor color) {
        return switch (color) {
            case WHITE -> wKing;
            case BLACK -> bKing;
        };
    }
}
