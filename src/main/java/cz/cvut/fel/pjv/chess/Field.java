package cz.cvut.fel.pjv.chess;

import java.util.Objects;

/**
 * A single square on a board.
 * Each square has its own unique coordinates.
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public class Field {
    public final int row;
    public final int column;

    /**
     * Initializes the Field
     * <p>
     *
     * @param row    a rank in the board
     * @param column a file in the board encoded by integer
     * @throws FieldOutOfRangeException if the given coordinates are not inside the board
     */
    public Field(int row, int column) throws FieldOutOfRangeException {
        if (row >= 0 && row <= Board.MAX_ROW) {
            this.row = row;
        } else {
            throw new FieldOutOfRangeException("expected 'row' to be in range " + 0 + ".." + Board.MAX_ROW + ", got " + row);
        }
        if (column >= 0 && column <= Board.MAX_COL) {
            this.column = column;
        } else {
            throw new FieldOutOfRangeException("expected 'column' to be in range " + 0 + ".." + Board.MAX_COL + ", got " + column);
        }
    }

    public Field plus(int rDiff, int cDiff) {
        Field pos;
        try {
            pos = new Field(this.row + rDiff, this.column + cDiff);
        } catch (FieldOutOfRangeException ignored) {
            pos = null;
        }
        return pos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return row == field.row && column == field.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }

    /**
     * Converts the field to the algebraic notation.
     */
    public String toAlgebraicNotation() {
        return (char)('a' + column) + Integer.toString(1 + (Board.MAX_ROW - row));
    }

    /**
     * Converts the field in algebraic notation to the field.
     */
    public static Field fromAlgebraicNotation(String algNotation) {
        if (algNotation.length() != 2) {
            throw new IllegalArgumentException("invalid square AN: expected 2 chars, got " + algNotation.length() + " chars");
        }
        char colChar = algNotation.charAt(0);
        char rowChar = algNotation.charAt(1);
        if (colChar < 'a' || (colChar - 'a') > Board.MAX_COL) {
            throw new IllegalArgumentException("invalid square AN: expected first char to be in [a-" + (char) ('a' + Board.MAX_COL) + "], got '" + colChar + "'");
        }
        if (rowChar < '1' || (rowChar - '1') > Board.MAX_ROW) {
            throw new IllegalArgumentException("invalid square AN: expected second char to be in [1-" + (char) ('1' + Board.MAX_ROW) + "], got '" + rowChar + "'");
        }
        return new Field(Board.MAX_ROW - (rowChar - '1'), colChar - 'a');
    }
}
