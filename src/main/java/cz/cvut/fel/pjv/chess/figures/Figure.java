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
    private final Field position;
    private final String image;
    private boolean isCaptured;
    private boolean isFirstMove = true;

    /**
     * Initializes the Figure
     * <p>
     *
     * @param color    the color of the piece
     * @param position the position on the board
     * @param image    the name of the .png file
     */
    public Figure(Color color, Field position, String image) {
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
     * @param board the board on which game is being played
     * @return list of available Fields in diagonal directions
     */
    public List<Field> getDiagonalDirections(Board board) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the Field on which the piece stands
     */
    public Field getPosition() {
        return this.position;
    }

    /**
     * Returns all Fields where the piece can move
     * <p>
     *
     * @param board the board on which game is being played
     * @return list of all Fields where the piece can move
     */
    abstract List<Field> getValidMoves(Board board);

    /**
     * Returns all Fields in vertical and horizontal directions where figure can possibly move
     * Each particular piece can select the moves from that list
     * <p>
     *
     * @param board the board on which game is being played
     * @return list of available Fields in vertical and horizontal directions
     */
    public List<Field> getVertAndHorDirections(Board board) {
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
     * @return the name of the .png file
     */
    public String getImage() {
        return image;
    }

    /**
     * @return true if it is the first move, false otherwise
     */
    public boolean isFirstMove() {
        return isFirstMove;
    }
}
