package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Color;
import cz.cvut.fel.pjv.chess.Field;

import java.util.List;

/**
 * A pawn piece
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Figure for the description of all methods
 */
public class Pawn extends Figure {
    public Pawn(Color color, Field position, String image) {
        super(color, position, image);
    }

    @Override
    public List<Field> getValidMoves(Board board) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return true if en passant is possible, false otherwise
     */
    public boolean isEnPassantPossible() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return true if it is the first move, false otherwise
     */
    public boolean isFirstMove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Promotes the pawn to the chosen piece
     */
    public void promotion(Figure figure) {
        throw new UnsupportedOperationException();
    }
}
