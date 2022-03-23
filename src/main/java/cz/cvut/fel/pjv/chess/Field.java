package cz.cvut.fel.pjv.chess;

import java.util.Objects;

/**
 * A single square on a board.
 * Each square has its own unique coordinates.
 *
 * @author  pucilpet@fel.cvut.cz
 * @author  rystidia@fel.cvut.cz
 * @version 1.0
 */
public class Field {
    public final int row;
    public final int column;

    /**
     * Initializes the Field
     * <p>
     * @param row a rank in the board
     * @param column a file in the board encoded by integer
     * @throws IllegalArgumentException if the given coordinates are not inside the board
     */
    Field(int row, int column) throws IllegalArgumentException{
        if (row >= 0 && row <= 7) {
            this.row = row;
        } else {
            throw new IllegalArgumentException();
        }
        if (column >= 0 && column <= 7) {
            this.column = column;
        } else {
            throw new IllegalArgumentException();
        }
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
}
