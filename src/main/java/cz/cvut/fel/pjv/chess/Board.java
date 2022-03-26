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
    Field[][] fields;
    private Figure bKing;
    private Figure wKing;

    /**
     * Initializes the board
     */
    public Board() {
        throw new UnsupportedOperationException();
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
        return (color == Color.WHITE) ? wKing : bKing;
    }

}
