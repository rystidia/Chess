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
        for (int row : Arrays.asList(-1, 0, 1)) {
            for (int column : Arrays.asList(-1, 0, 1)) {
                if (row == 0 && column == 0) {
                    continue;
                }
                addValidMove(validMoves, row, column);
            }
        }
        return validMoves;
    }

    /**
     * @return true if castling is possible, false otherwise
     */
    public boolean isCastlingPossibleWith(Rook rook) {
        if (!isFirstMove() || !rook.isFirstMove()){
            return false;
        }
        int dir = 1;
        if(getPosition().row - rook.getPosition().row > 0){
            dir = -1;
        }
        Field kingDest = getPosition().plus(0, dir * 2);
        Field rookDest = getPosition().plus(0, dir);
        Figure blockingFig1 = board.getFigure(kingDest);
        Figure blockingFig2 = board.getFigure(rookDest);
        return blockingFig1 == null && blockingFig2 == null;
    }

    /**
     * @return true if the king is in check, false otherwise
     */
    public boolean isInCheck() {
        throw new UnsupportedOperationException();
    }
}
