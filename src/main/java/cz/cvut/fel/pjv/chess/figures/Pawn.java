package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.FieldOld;

import java.util.List;

public class Pawn extends Figure {
    public Pawn(int color, FieldOld position, String image) {
        super(color, position, image);
    }

    @Override
    public List<FieldOld> getValidMoves() {
        throw new UnsupportedOperationException();
    }

    public boolean isEnPassantPossible() {
        throw new UnsupportedOperationException();
    }

    public boolean isFirstMove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean move() {
        throw new UnsupportedOperationException();
    }

    public void promotion(Figure figure) {
        throw new UnsupportedOperationException();
    }
}
