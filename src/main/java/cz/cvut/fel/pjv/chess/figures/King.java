package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.MyColor;
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
final public class King extends Figure {
    public King(MyColor color, Board board) {
        super(color, board);
    }

    @Override
    public Figure clone(Board dstBoard) {
        King fig = new King(getColor(), dstBoard);
        fig.isFirstMove = isFirstMove;
        fig.setPosition(getPosition());
        return fig;
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
        for (boolean queenSide : Arrays.asList(false, true)) {
            if (!canCastlingBeAvailable(queenSide)) {
                continue;
            }
            Rook rook = getRookForCastling(queenSide);
            Field kingDest = getPosition().plus(0, queenSide ? -2 : 2);
            if (isCastlingPossibleWith(rook)) {
                validMoves.add(kingDest);
            }
        }
    }

    public boolean canCastlingBeAvailable(boolean queenSide) {
        if (!isFirstMove()) {
            return false;
        }
        Rook rook = getRookForCastling(queenSide);
        if (rook == null) {
            return false;
        }
        return rook.isFirstMove() && rook.getColor() == getColor();
    }

    public boolean isCastlingPossibleWith(Rook rook) {
        int dir = rook.getPosition().column - getPosition().column < 0 ? -1 : 1;
        Figure blockingFigure;
        for (int i = 1; ; i++) {
            Field between = getPosition().plus(0, dir * i);
            blockingFigure = board.getFigure(between);
            if (blockingFigure != null) {
                break;
            }
            if (i <= 2) {
                Board newBoard = board.simulateMove(this, between);
                if (newBoard.getKing(getColor()).isInCheck()) {
                    return false;
                }
            }
        }
        return blockingFigure == rook;
    }

    @Override
    public void move(Field toPos) {
        if (getPosition() != null && Math.abs(getPosition().column - toPos.column) > 1) {
            Rook castlingRook = getRookForCastling(toPos.column < 4);
            if (castlingRook != null) {
                board.moveFigure(castlingRook, new Field(toPos.row, (getPosition().column + toPos.column) / 2), false);
            }
        }
        super.move(toPos);
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
                }
            }
        }
        return false;
    }

    public Rook getRookForCastling(boolean queenSide) {
        int row = getColor() == MyColor.WHITE ? Board.MAX_ROW : 0;
        int column = queenSide ? 0 : Board.MAX_COL;
        Figure fig = board.getFigure(new Field(row, column));
        return (fig instanceof Rook) ? (Rook) fig : null;
    }
}
