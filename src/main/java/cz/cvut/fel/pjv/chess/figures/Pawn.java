package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.MyColor;
import cz.cvut.fel.pjv.chess.Field;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A pawn piece
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Figure for the description of all methods
 */
public class Pawn extends Figure {
    private boolean doubleAdvance = false;

    public Pawn(MyColor color, Board board) {
        super(color, board);
    }

    @Override
    public Set<Field> getValidMoves() {
        int dir = (getColor() == MyColor.WHITE) ? -1 : 1; // direction
        Set<Field> validMoves = new HashSet<>();
        addValidMoveIfNull(validMoves, dir, 0);
        for (int j : Arrays.asList(-1, 1)) { // occupied position
            addValidMoveIfBlockedByOpp(validMoves, dir, j);

            //enPassant
            Field pos = getPosition().plus(0, j);
            if (pos != null) {
                Figure bf = board.getFigure(pos); // blocking figure
                if (bf != null) {
                    if (bf.getColor() != getColor() && bf instanceof Pawn && ((Pawn) bf).doubleAdvance()) {
                        addValidMoveIfNull(validMoves, dir, j);
                    }
                }
            }
            //end of enPassant

        }
        if (isFirstMove()) {
            addValidMoveIfNull(validMoves, 2 * dir, 0);
        }
        return validMoves;
    }

    /**
     * Notes that if pawn jumped over a Field, it can be captured by en passant
     */
    @Override
    public void setPosition(Field position) {
        doubleAdvance = getPosition() != null && Math.abs(getPosition().row - position.row) > 1;
        super.setPosition(position);
    }

    /**
     * @return true if en passant is possible, false otherwise
     */
    public boolean doubleAdvance() {
        return doubleAdvance;
    }

    /**
     * Promotes the pawn to the chosen piece
     */
    public void promotion(Figure figure) {
        throw new UnsupportedOperationException();
    }

    private void addValidMoveIfNull(Set<Field> validMoves, int row, int column) {
        Field pos = getPosition().plus(row, column);
        if (pos == null) {
            return;
        }
        Figure blockingFig = board.getFigure(pos);
        if (blockingFig == null) {
            validMoves.add(pos);
        }
    }

    private void addValidMoveIfBlockedByOpp(Set<Field> validMoves, int row, int column) {
        Field pos = getPosition().plus(row, column);
        if (pos == null) {
            return;
        }
        Figure blockingFig = board.getFigure(pos);
        if (blockingFig != null && blockingFig.getColor() != getColor()) {
            validMoves.add(pos);
        }
    }
}
