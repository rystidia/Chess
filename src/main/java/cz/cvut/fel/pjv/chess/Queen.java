package cz.cvut.fel.pjv.chess;

public class Queen extends Figure {
    public Queen(int color, Field position, String image) {
        super(color, position, image);
    }

    public Field[] getValidMoves() {
        throw new UnsupportedOperationException();
    }

    public boolean move() {
        throw new UnsupportedOperationException();
    }
}

