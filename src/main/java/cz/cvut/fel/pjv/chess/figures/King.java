package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Color;
import cz.cvut.fel.pjv.chess.Field;

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
        addCastling(validMoves);
        return validMoves;
    }

    public void addCastling(Set<Field> validMoves) {
        int row = getColor() == Color.WHITE ? Board.MAX_ROW : 0;
        for (int column : Arrays.asList(0, Board.MAX_COL)) {
            Field pos = new Field(row, column);
            Figure fig = board.getFigure(pos);
            if (fig instanceof Rook && fig.getColor() == getColor()) {
                if (!isFirstMove() || !fig.isFirstMove()){
                    break;
                }
                int dir = getPosition().column - fig.getPosition().column > 0 ? -1 : 1;
                Field kingDest = getPosition().plus(0, dir * 2);

                Figure blockingFigure = null;
                for (int i = 1; blockingFigure == null; i++) {
                    Field between = getPosition().plus(0, dir * i);
                    blockingFigure = board.getFigure(between);
                }
                if (blockingFigure == fig) {
                    validMoves.add(kingDest);
                }
            }
        }
    }

    /**
     * @return true if the king is in check, false otherwise
     */
    public boolean isInCheck() {
        for (int r = 0; r <= Board.MAX_ROW; r++) {
            for (int c = 0; c <= Board.MAX_COL; c++) {
                Field field = new Field(r, c);
                Figure fig = board.getFigure(field);
                if (fig == null || fig.getColor() == getColor()) {
                    continue;
                }
                if (fig.getValidMoves().contains(getPosition())) {
                    return true;
                };
            }
        }
        return false;
    }
}
