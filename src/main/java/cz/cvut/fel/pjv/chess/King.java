package cz.cvut.fel.pjv.chess;

public class King extends Figure{
    public King(int color, Field position, String image) {
        super(color, position, image);
    }

    public Field[] getValidMoves() {
        throw new UnsupportedOperationException();
    }

    public boolean isCastlingPossible() {
        throw new UnsupportedOperationException();
    }

    public boolean isInCheck() {
        throw new UnsupportedOperationException();
    }

    public boolean move() {
        throw new UnsupportedOperationException();
    }
}

