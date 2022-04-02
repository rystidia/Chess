package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Color;
import cz.cvut.fel.pjv.chess.Field;
import cz.cvut.fel.pjv.chess.players.Player;

import java.util.List;

/**
 * An abstract model of a chess piece on the board
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public abstract class Figure {
    private final Color color;
    private final Board board;
    private Field position;
    private boolean isCaptured;
    private boolean isFirstMove = true;

    /**
     * Initializes the Figure
     * <p>
     *
     * @param color    the color of the piece
     * @param board the board
     */
    public Figure(Color color, Board board) {
        this.color = color;
        this.board = board;
        this.isCaptured = false;
    }

    /**
     * Captures the piece
     */
    public void die() {
        this.isCaptured = true;
    }

    /**
     * @return the Field on which the piece stands
     */
    public Field getPosition() {
        return position;
    }

    /**
     * Returns all Fields where the piece can move
     * <p>
     *
     * @return list of all Fields where the piece can move
     */
    public abstract List<Field> getValidMoves();

    /**
     * Returns all Fields in diagonal directions where the piece can possibly move
     * Each particular piece can select the moves from that list
     * <p>
     *
     * @return list of available Fields in diagonal directions
     */
    protected List<Field> getDiagonalDirections() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns all Fields in vertical and horizontal directions where figure can possibly move
     * Each particular piece can select the moves from that list
     * <p>
     *
     * @return list of available Fields in vertical and horizontal directions
     */
    protected List<Field> getVertAndHorDirections() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return true if the piece is captured, false otherwise
     */
    public boolean isCaptured() {
        return this.isCaptured;
    }

    /**
     * Moves the piece to the given Field
     * <p>
     *
     * @param target a target Field
     * @return true if move was done, false otherwise
     */
    public boolean move(Field target) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the {@link Color color} of the piece
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return true if it is the first move, false otherwise
     */
    public boolean isFirstMove() {
        return isFirstMove;
    }
}
