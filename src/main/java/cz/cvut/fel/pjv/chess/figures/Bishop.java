package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.MyColor;
import cz.cvut.fel.pjv.chess.Field;

import java.util.Set;

/**
 * A bishop piece
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Figure for the description of all methods
 */
public class Bishop extends Figure {
    public Bishop(MyColor color, Board board) {
        super(color, board);
    }

    @Override
    public Figure clone(Board dstBoard) {
        Bishop fig = new Bishop(getColor(), dstBoard);
        fig.isFirstMove = isFirstMove;
        fig.setPosition(getPosition());
        return fig;
    }

    @Override
    public Set<Field> getValidMoves() {
        return super.getDiagonalDirections();
    }
}
