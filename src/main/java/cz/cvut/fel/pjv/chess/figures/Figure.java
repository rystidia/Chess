package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.MyColor;
import cz.cvut.fel.pjv.chess.Field;

import java.util.*;

/**
 * An abstract model of a chess piece on the board
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public abstract class Figure {
    private static final Map<Character, Class<? extends Figure>> charToFigClass = new HashMap<>() {{
        put('P', Pawn.class);
        put('N', Knight.class);
        put('B', Bishop.class);
        put('R', Rook.class);
        put('Q', Queen.class);
        put('K', King.class);
    }};
    private static final Map<Class<? extends Figure>, Character> figClassToChar = new HashMap<>() {{
        for (Entry<Character, Class<? extends Figure>> entry : charToFigClass.entrySet()) {
            put(entry.getValue(), entry.getKey());
        }
    }};
    protected final Board board;
    private final MyColor color;
    protected boolean isFirstMove = true;
    private Field position;

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
     * @return name of figure in PGN figure naming notation
     */
    public static char getCharacterByFigureClass(Class<? extends Figure> figClass) {
        return figClassToChar.get(figClass);
    }

    /**
     * @param ch name of figure in PGN figure naming notation
     * @return class of the figure given by name
     */
    public static Class<? extends Figure> getFigureClassByCharacter(char ch) {
        return charToFigClass.get(ch);
    }

    public abstract Figure clone(Board dstBoard);

    /**
     * Moves the piece to the given field
     */
    public void move(Field toPos) {
        isFirstMove = getPosition() == null;
        board.setFigure(toPos, this);
        setPosition(toPos);
    }

    /**
     * @return the Field on which the piece stands
     */
    public Field getPosition() {
        return position;
    }

    public void setPosition(Field position) {
        this.position = position;
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

    /**
     * Adds a field given by row and column to given set, if field is empty or is occupied by opponent's piece
     */
    protected void addValidMove(Set<Field> validMoves, int row, int column) {
        Field pos = getPosition().plus(row, column);
        if (pos == null) {
            return;
        }
        Figure blockingFig = board.getFigure(pos);
        if (blockingFig == null || blockingFig.getColor() != getColor()) {
            validMoves.add(pos);
        }
    }

    /**
     * @return true if figure has valid moves, false otherwise
     */
    public boolean hasValidMoves() {
        return !board.getValidMoves(this).isEmpty();
    }
}
