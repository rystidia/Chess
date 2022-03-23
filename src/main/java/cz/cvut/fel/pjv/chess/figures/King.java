package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.FieldOld;

import java.util.List;

public class King extends Figure{
    public King(int color, FieldOld position, String image) {
        super(color, position, image);
    }

    @Override
    public List<FieldOld> getValidMoves() {
        throw new UnsupportedOperationException();
    }

    public boolean isCastlingPossible() {
        throw new UnsupportedOperationException();
    }

    public boolean isInCheck() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean move() {
        throw new UnsupportedOperationException();
    }
}

