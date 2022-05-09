package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * A board for a chess game
 * Board is nothing, but a matrix of the Fields
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public class Board {
    public static final int MAX_ROW = 7;
    public static final int MAX_COL = 7;

    private final Figure[][] board;
    private King whiteKing;
    private King blackKing;

    /**
     * Initializes the board
     */
    public Board() {
        this.board = new Figure[MAX_ROW + 1][MAX_COL + 1];
    }

    public Board(Board srcBoard) {
        this();
        for (int r = 0; r <= MAX_ROW; r++) {
            for (int c = 0; c <= MAX_COL; c++) {
                Figure fig = srcBoard.board[r][c];
                if (fig != null) {
                    fig = fig.clone(this);
                }
                this.board[r][c] = fig;
            }
        }
    }

    public Figure getFigure(Field pos) {
        return board[pos.row][pos.column];
    }

    public void setFigure(Field pos, Figure figure) {
        if (figure != null) figure.setPosition(pos);
        board[pos.row][pos.column] = figure;
    }

    public void moveFigure(Figure figure, Field toPos) {
        if (figure.getPosition() != null) {
            if (getFigure(figure.getPosition()) != figure) {
                throw new IllegalArgumentException("provided figure was not found where it should be");
            }
            setFigure(figure.getPosition(), null);
        }
        figure.move(toPos);
    }

    /**
     * Places figures to the fields
     */
    public void initialPosition() {
        for (MyColor color : Arrays.asList(MyColor.BLACK, MyColor.WHITE)) {
            int row = color == MyColor.WHITE ? 7 : 0;
            moveFigure(new Rook(color, this), new Field(row, 0));
            moveFigure(new Rook(color, this), new Field(row, 7));
            moveFigure(new Knight(color, this), new Field(row, 1));
            moveFigure(new Knight(color, this), new Field(row, 6));
            moveFigure(new Bishop(color, this), new Field(row, 2));
            moveFigure(new Bishop(color, this), new Field(row, 5));
            moveFigure(new Queen(color, this), new Field(row, 3));
            moveFigure(new King(color, this), new Field(row, 4));
        }
        for (int i = 0; i <= MAX_COL; i++) {
            moveFigure(new Pawn(MyColor.WHITE, this), new Field(6, i));
            moveFigure(new Pawn(MyColor.BLACK, this), new Field(1, i));
        }
    }

    /**
     * Returns all valid moves for given figure, checks if king is not in check after them
     */
    public Set<Field> getValidMoves(Figure figure) {
        Set<Field> validMoves = figure.getValidMoves();
        for (Iterator<Field> iterator = validMoves.iterator(); iterator.hasNext(); ) {
            Field pos = iterator.next();
            Board newBoard = simulateMove(figure, pos);
            if (newBoard.getKing(figure.getColor()).isInCheck()) {
                iterator.remove();
            }
        }
        return validMoves;
    }

    public King getKing(MyColor color) {
        if (whiteKing != null && color ==MyColor.WHITE) return whiteKing;
        if (blackKing != null && color == MyColor.BLACK) return blackKing;
        King king;
        for (Figure[] row : board) {
            for (Figure fig : row) {
                if (fig instanceof King && fig.getColor() == color) {
                    king = (King) fig;
                    if (color == MyColor.WHITE && whiteKing == null) whiteKing = king;
                    if (color == MyColor.BLACK && blackKing == null) blackKing = king;
                    return (King) fig;
                }
            }
        }
        return null;
    }

    public Board simulateMove(Figure figure, Field toPos) {
        Board newBoard = new Board(this);
        Figure newFig = newBoard.getFigure(figure.getPosition());
        newBoard.moveFigure(newFig, toPos);
        return newBoard;
    }

    public int getNumOfFigs(MyColor color) {
        int ret = 0;
        for (Figure[] row : board) {
            for (Figure fig : row) {
                if (fig != null && fig.getColor() == color)
                    ret++;
            }
        }
        return ret;
    }

    public int getNumOfSameFigs(Figure figure) {
        int ret = 0;
        for (Figure[] row : board) {
            for (Figure fig : row) {
                if (fig != null && fig.getColor() == figure.getColor() && fig.getClass().getSimpleName().equals(figure.getClass().getSimpleName()))
                    ret++;
            }
        }
        return ret;
    }

    public boolean canPlaceFigure(Figure figure) {
        MyColor c = figure.getColor();
        int extraKnights = Math.max(getNumOfSameFigs(new Knight(c, null)) - 2, 0);
        int extraBishops = Math.max(getNumOfSameFigs(new Bishop(c, null)) - 2, 0);
        int extraQueens = Math.max(getNumOfSameFigs(new Queen(c, null)) - 1, 0);
        int extraRooks = Math.max(getNumOfSameFigs(new Rook(c, null)) - 2, 0);
        int extraFigs = extraRooks + extraQueens + extraBishops + extraKnights;
        return extraFigs < 8 - getNumOfSameFigs(new Pawn(c, null));
    }
}
