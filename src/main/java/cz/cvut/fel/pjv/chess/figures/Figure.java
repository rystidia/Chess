package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.MyColor;
import cz.cvut.fel.pjv.chess.Field;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * An abstract model of a chess piece on the board
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public abstract class Figure {
    private final MyColor color;
    protected static Board board;
    private Field position;
    private boolean isFirstMove = true;

    private Field savedPosition;
    private boolean savedIsFirstMove;

    /**
     * Initializes the Figure
     * <p>
     *
     * @param color the color of the piece
     * @param board the board
     */
    public Figure(MyColor color, Board board) {
        this.color = color;
        this.board = board;
    }

    /**
     * Captures the piece
     */
    public void move(Field toPos) {
        isFirstMove = getPosition() == null;
        board.setFigure(toPos, this);
        setPosition(toPos);
    }

    public void setPosition(Field position) {
        this.position = position;
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
    public abstract Set<Field> getValidMoves();

    /**
     * Returns all Fields in diagonal directions where the piece can possibly move
     * Each particular piece can select the moves from that list
     * <p>
     *
     * @return list of available Fields in diagonal directions
     */
    protected Set<Field> getDiagonalDirections() {
        Set<Field> fields = new HashSet<>();
        for (int rDiff : Arrays.asList(-1, 1)) {
            for (int cDiff : Arrays.asList(-1, 1)) {
                fields.addAll(getFieldsInDirection(rDiff, cDiff));
            }
        }
        return fields;
    }

    /**
     * Returns all Fields in vertical and horizontal directions where figure can possibly move
     * Each particular piece can select the moves from that list
     * <p>
     *
     * @return list of available Fields in vertical and horizontal directions
     */
    protected Set<Field> getVertAndHorDirections() {
        Set<Field> fields = new HashSet<>();
        for (int sign : Arrays.asList(-1, 1)) {
            for (boolean changeRow : Arrays.asList(false, true)) {
                int rDiff = 0, cDiff = 0;
                if (changeRow) {
                    rDiff = sign;
                } else {
                    cDiff = sign;
                }
                fields.addAll(getFieldsInDirection(rDiff, cDiff));
            }
        }
        return fields;
    }

    private Set<Field> getFieldsInDirection(int rDiff, int cDiff) {
        Set<Field> fields = new HashSet<>();
        int rAvail = Board.MAX_ROW;
        if (rDiff < 0) {
            rAvail = position.row;
        } else if (rDiff > 0) {
            rAvail -= position.row;
        }
        int cAvail = Board.MAX_COL;
        if (cDiff < 0) {
            cAvail = position.column;
        } else if (cDiff > 0) {
            cAvail -= position.column;
        }
        int avail = Math.min(rAvail, cAvail);
        for (int i = 1; i <= avail; i++) {
            Field pos = position.plus(i * rDiff, i * cDiff);
            Figure blockingFig = board.getFigure(pos);
            if (blockingFig != null) {
                if (blockingFig.getColor() != color) {
                    fields.add(pos);
                }
                break;
            }
            fields.add(pos);
        }
        return fields;
    }

    /**
     * @return the {@link MyColor color} of the piece
     */
    public MyColor getColor() {
        return color;
    }

    /**
     * @return true if it is the first move, false otherwise
     */
    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void addValidMove(Set<Field> validMoves, int row, int column){
        Field pos = getPosition().plus(row, column);
        if (pos == null){
            return;
        }
        Figure blockingFig = board.getFigure(pos);
        if (blockingFig == null || blockingFig.getColor() != getColor()) {
            validMoves.add(pos);
        }
    }

    public boolean hasValidMoves(){
        return !getValidMoves().isEmpty();
    }

    public void saveState(){
        savedPosition = position;
        savedIsFirstMove = isFirstMove;
    }

    public void restoreState(){
        position = savedPosition;
        isFirstMove = savedIsFirstMove;
    }
}
