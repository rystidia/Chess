package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
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
    private final int color;
    private final Field position;
    private final String image;
    private boolean isCaptured;

    /**
     * Initializes the Figure
     * <p>
     *
     * @param color    The color of the piece
     * @param position The position on the board
     * @param image    The name of the .png file
     */
    public Figure(int color, Field position, String image) {
        this.color = color;
        this.position = position;
        this.image = image;
        this.isCaptured = false;
    }

    /**
     * Captures the piece
     */
    public void die() {
        this.isCaptured = true;
    }

    /**
     * Returns all Fields in diagonal directions where the piece can possibly move
     * Each particular piece can select the moves from that list
     * <p>
     *
     * @param board The board on which game is being played
     * @return List of available Fields in diagonal directions
     */
    public List<Field> getDiagonalDirections(Board board) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return The Field on which the piece stands
     */
    public Field getPosition() {
        return this.position;
    }

    /**
     * Returns all Fields where the piece can move
     * <p>
     *
     * @param board The board on which game is being played
     * @return List of all Fields where the piece can move
     */
    public List<Field> getValidMoves(Board board) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns all Fields in vertical and horizontal directions where figure can possibly move
     * Each particular piece can select the moves from that list
     * <p>
     *
     * @param board The board on which game is being played
     * @return List of available Fields in vertical and horizontal directions
     */
    public List<Field> getVertAndHorDirections(Board board) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return True if the piece is captured, false otherwise
     */
    public boolean isCaptured() {
        return this.isCaptured;
    }

    /**
     * Moves the piece to the given Field
     * <p>
     *
     * @param target A target Field
     * @return True if move was done, false otherwise
     */
    public boolean move(Field target) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return The {@link Player color} of the piece
     */
    public int getColor() {
        return color;
    }

    /**
     * @return The name of the .png file
     */
    public String getImage() {
        return image;
    }
}
