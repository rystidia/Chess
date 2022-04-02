package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Color;
import cz.cvut.fel.pjv.chess.Field;

import java.util.List;

/**
 * A rook piece
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Figure for the description of all methods
 */
public class Rook extends Figure {
    public Rook(Color color, Board board) {
        super(color, board);
    }

    @Override
    public List<Field> getValidMoves() {
        throw new UnsupportedOperationException();
    }
}
