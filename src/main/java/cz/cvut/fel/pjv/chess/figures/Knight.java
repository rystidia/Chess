package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.FieldOld;

import java.util.List;

public class Knight extends Figure{
    public Knight(int color, FieldOld position, String image) {
        super(color, position, image);
    }

    @Override
    public List<FieldOld> getValidMoves() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean move() {
        throw new UnsupportedOperationException();
    }
}
