package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.MyColor;
import cz.cvut.fel.pjv.chess.Field;

import java.util.Set;

/**
 * A rook piece
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Figure for the description of all methods
 */
final public class Rook extends Figure {
    public Rook(MyColor color, Board board) {
        super(color, board);
    }

    @Override
    public Figure clone(Board dstBoard) {
        Rook fig = new Rook(getColor(), dstBoard);
        fig.isFirstMove = isFirstMove;
        fig.setPosition(getPosition());
        return fig;
    }
    @Override
    public Set<Field> getValidMoves() {
        return super.getVertAndHorDirections();
    }
}
