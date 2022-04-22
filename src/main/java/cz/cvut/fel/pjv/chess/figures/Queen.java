package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.MyColor;
import cz.cvut.fel.pjv.chess.Field;

import java.util.HashSet;
import java.util.Set;

/**
 * A queen piece
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Figure for the description of all methods
 */
public class Queen extends Figure {
    public Queen(MyColor color, Board board) {
        super(color, board);
    }

    @Override
    public Set<Field> getValidMoves() {
        Set<Field> validMoves = new HashSet<>();
        validMoves.addAll(super.getDiagonalDirections());
        validMoves.addAll(super.getVertAndHorDirections());
        return validMoves;
    }
}
