package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Field;

import java.util.List;

/**
 * A king piece
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Figure for the descrition of all methods
 */
public class King extends Figure {
    public King(int color, Field position, String image) {
        super(color, position, image);
    }

    @Override
    public List<Field> getValidMoves(Board board) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return True if castling is possible, false otherwise
     */
    public boolean isCastlingPossible() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return True if the king is in check, false otherwise
     */
    public boolean isInCheck() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean move(Field target) {
        throw new UnsupportedOperationException();
    }
}

