package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Field;

import java.util.List;

public class Rook extends Figure {
    public Rook(int color, Field position, String image) {
        super(color, position, image);
    }

    @Override
    public List<Field> getValidMoves() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean move() {
        throw new UnsupportedOperationException();
    }
}
