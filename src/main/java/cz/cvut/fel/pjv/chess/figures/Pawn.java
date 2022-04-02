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
    public Pawn(Color color, Field position) {
        super(color, position);
    }

    @Override
    public List<Field> getValidMoves(Board board) {
        throw new UnsupportedOperationException();
    }

    /**
     * Notes that if pawn jumped over a Field, it can be captured by en passant
     *
     * @see #Figure for the full description of this method
     */
    @Override
    public boolean move(Field target) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return true if en passant is possible, false otherwise
     */
    public boolean isEnPassantPossible() {
        throw new UnsupportedOperationException();
    }

    /**
     * Promotes the pawn to the chosen piece
     */
    public void promotion(Figure figure) {
        throw new UnsupportedOperationException();
    }
}
