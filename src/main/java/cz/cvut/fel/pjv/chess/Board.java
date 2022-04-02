package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;

import java.util.List;

/**
 * A board for a chess game
 * Board is nothing, but a matrix of the Fields
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public class Board {
    public static final int ROWS = 8;
    public static final int COLS = 8;

    private final Figure[][] board;
    private Figure bKing;
    private Figure wKing;

    /**
     * Initializes the board
     */
    public Board() {
        this.board = new Figure[ROWS][COLS];
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
    public List<Field> getValidMoves(Figure figure) {
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
