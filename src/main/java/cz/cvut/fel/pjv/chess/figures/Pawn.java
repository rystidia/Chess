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
            Field capturingMove = getPosition().plus(dir, j);
            if (capturingMove == null) {
                continue;
            }
            Field capturePos = getCapturedFieldByMove(capturingMove);
            if (capturePos != null) {
                validMoves.add(capturingMove);
            }

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
    public void move(Field toPos) {
        doubleAdvance = getPosition() != null && Math.abs(getPosition().row - toPos.row) > 1;
        Field capturePos = getCapturedFieldByMove(toPos);
        if (capturePos != null) {
            board.setFigure(capturePos, null);
        }
        super.move(toPos);
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

    public Field getCapturedFieldByMove(Field dest) {
        if (getPosition() == null) {
            return null;
        }
        Figure occupyingFig = board.getFigure(dest);
        if (occupyingFig != null) {
            return occupyingFig.getColor() != getColor() ? dest : null;
        }
        int dir = (getColor() == MyColor.WHITE) ? 1 : -1; // direction
        Field enPassantCaptPos = dest.plus(dir, 0);
        Figure bf = board.getFigure(enPassantCaptPos);
        if (bf != null && bf.getColor() != getColor() && bf instanceof Pawn && ((Pawn) bf).doubleAdvance()) {
            return enPassantCaptPos;
        }
        return null;
    }

    public boolean moveLeadsToPromotion(Field dest) {
        return (getColor() == MyColor.WHITE && dest.row == 7) || (getColor() == MyColor.BLACK && dest.row == 0);
    }
}
