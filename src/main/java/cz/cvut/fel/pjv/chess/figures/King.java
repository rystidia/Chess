package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Color;
import cz.cvut.fel.pjv.chess.Field;

import java.util.Set;

/**
 * A king piece
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Figure for the description of all methods
 */
public class King extends Figure {
    public King(Color color, Board board) {
        super(color, board);
    }

    @Override
    public Set<Field> getValidMoves() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return true if castling is possible, false otherwise
     */
    public boolean isCastlingPossibleWith(Rook rook) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return true if the king is in check, false otherwise
     */
    public boolean isInCheck() {
        throw new UnsupportedOperationException();
    }
}
