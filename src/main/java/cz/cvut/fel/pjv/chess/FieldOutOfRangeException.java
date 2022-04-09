package cz.cvut.fel.pjv.chess;

public class FieldOutOfRangeException extends IllegalArgumentException {
    public FieldOutOfRangeException(String message) {
        super(message);
    }
}
