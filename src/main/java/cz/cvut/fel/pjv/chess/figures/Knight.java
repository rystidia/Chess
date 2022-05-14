package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.MyColor;
import cz.cvut.fel.pjv.chess.Field;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A knight piece
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Figure for the description of all methods
 */
final public class Knight extends Figure {
    public Knight(MyColor color, Board board) {
        super(color, board);
    }

    @Override
    public Figure clone(Board dstBoard) {
        Knight fig = new Knight(getColor(), dstBoard);
        fig.isFirstMove = isFirstMove;
        fig.setPosition(getPosition());
        return fig;
    }

    @Override
    public Set<Field> getValidMoves() {
        Set<Field> validMoves = new HashSet<>();
        for (int oneDiff : Arrays.asList(-1, 1)) {
            for (int twoDiff : Arrays.asList(-2, 2)) {
                addValidMove(validMoves, oneDiff, twoDiff);
                addValidMove(validMoves, twoDiff, oneDiff);
            }
        }
        return validMoves;
    }
}
