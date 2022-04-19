package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;
import cz.cvut.fel.pjv.chess.figures.Rook;

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

    protected void setFigure(Field pos, Figure figure) {
        board[pos.row][pos.column] = figure;
    }

    public void moveFigure(Figure figure, Field toPos) {
        if (figure.getPosition() != null && getFigure(figure.getPosition()) != figure) {
            throw new IllegalArgumentException("provided figure was not found where it should be");
        }
        if (getFigure(toPos) != null) {
            throw new UnsupportedOperationException("toPos already occupied, capturing is not implemented yet");
        }
        setFigure(toPos, figure);
        figure.setPosition(toPos);
    }

    /**
     * Places figures to the fields
     */
    public void placeFigures() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns all valid moves for given figure, checks if king is not in check after them
     */
    public Set<Field> getValidMoves(Figure figure) {
        throw new UnsupportedOperationException();
    }

    public Figure getKing(Color color) {
        Figure king = null;
        switch (color) {
            case WHITE:
                king = wKing;
                break;
            case BLACK:
                king = bKing;
                break;
        }
        return king;
    }

}
