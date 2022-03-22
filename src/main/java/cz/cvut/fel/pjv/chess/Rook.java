package cz.cvut.fel.pjv.chess;

public class Rook extends Figure {
    public Rook(int color, Field position, String image) {
        super(color, position, image);
    }

    public Field[] getValidMoves() {
        throw new UnsupportedOperationException();
    }

    public boolean move() {
        throw new UnsupportedOperationException();
    }
}
