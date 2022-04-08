package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Color;
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
    public boolean isEnPassantPossible = false;

    public Pawn(Color color, Board board) {
        super(color, board);
    }

    @Override
    public Set<Field> getValidMoves() {
        int dir = (getColor() == Color.WHITE) ? -1 : 1; // direction
        Set<Field> validMoves = new HashSet<>();
        try {
            Field pos = getPosition().plus(dir, 0);
            if (getBoard().getFigure(pos) == null) {
                validMoves.add(pos);
            }
            for (int j : Arrays.asList(-1, 1)) { // occupied position
                try {
                    Field oPos = getPosition().plus(dir, j);
                    Figure blockingFig = getBoard().getFigure(oPos);
                    if (blockingFig != null && blockingFig.getColor() != getColor()) {
                        validMoves.add(oPos);
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }
            if (isFirstMove()) {
                Field posAcross = getPosition().plus(2 * dir, 0);
                if (getBoard().getFigure(posAcross) == null) {
                    validMoves.add(posAcross);
                }
            }
        } catch (IllegalArgumentException ignored) {
        }
        return validMoves;
    }

    /**
     * Notes that if pawn jumped over a Field, it can be captured by en passant
     */
    @Override
    public void setPosition(Field position) {
//        if (Math.abs(super.getPosition().row - position.row)>1){
//            isEnPassantPossible = true;
//        }
        super.setPosition(position);
    }

    /**
     * @return true if en passant is possible, false otherwise
     */
    public boolean isEnPassantPossible() {
        return isEnPassantPossible;
    }

    /**
     * Promotes the pawn to the chosen piece
     */
    public void promotion(Figure figure) {
        throw new UnsupportedOperationException();
    }
}
