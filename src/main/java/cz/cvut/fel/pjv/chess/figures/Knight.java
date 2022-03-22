package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Field;

import java.util.List;

public class Knight extends Figure{
    public Knight(int color, Field position, String image) {
        super(color, position, image);
    }

    public List<Field> getValidMoves() {
        throw new UnsupportedOperationException();
    }

    public boolean move() {
        throw new UnsupportedOperationException();
    }
}
