package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Color;
import cz.cvut.fel.pjv.chess.Field;

import java.util.List;

/**
 * A queen piece
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Figure for the description of all methods
 */
public class Queen extends Figure {
    public Queen(Color color, Field position) {
        super(color, position);
    }

    @Override
    public List<Field> getValidMoves(Board board) {
        throw new UnsupportedOperationException();
    }
}
