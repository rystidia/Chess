package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Color;
import cz.cvut.fel.pjv.chess.Field;
import cz.cvut.fel.pjv.chess.FieldOutOfRangeException;

import java.util.Arrays;
import java.util.HashSet;
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
        Set<Field> validMoves = new HashSet<>();
        for (int i : Arrays.asList(-1, 0, 1)) {
            for (int j : Arrays.asList(-1, 0, 1)) {
                if (i == 0 && j == 0) {
                    continue;
                }
                Field pos;
                try {
                    pos = getPosition().plus(i, j);
                } catch (FieldOutOfRangeException ignored) {
                    continue;
                }
                Figure blockingFig = getBoard().getFigure(pos);
                if (blockingFig == null || blockingFig.getColor() != getColor()) {
                    validMoves.add(pos);
                }
            }
        }
        return validMoves;
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
