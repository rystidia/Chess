package cz.cvut.fel.pjv.chess;

public class Pawn extends Figure {
    public Pawn(int color, Field position, String image) {
        super(color, position, image);
    }

    public Field[] getValidMoves() {
        throw new UnsupportedOperationException();
    }

    public boolean isEnPassantPossible() {
        throw new UnsupportedOperationException();
    }

    public boolean isFirstMove() {
        throw new UnsupportedOperationException();
    }

    public boolean move() {
        throw new UnsupportedOperationException();
    }

    public void promotion(Figure figure) {
        throw new UnsupportedOperationException();
    }
}
