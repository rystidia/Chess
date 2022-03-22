package cz.cvut.fel.pjv.chess;

public abstract class Figure {
    private final int color;
    private final Field position;
    private final String image;

    public Figure(int color, Field position, String image) {
        this.color = color;
        this.position = position;
        this.image = image;
    }

    public void die() {
        throw new UnsupportedOperationException();
    }

    public Field[] getDiagonalDirections() {
        throw new UnsupportedOperationException();
    }

    public Field getPosition() {
        throw new UnsupportedOperationException();
    }

    public Field[] getValidMoves() {
        throw new UnsupportedOperationException();
    }

    public Field[] getVertAndHorDirections() {
        throw new UnsupportedOperationException();
    }

    public boolean isCaptured() {
        throw new UnsupportedOperationException();
    }

    public boolean move() {
        throw new UnsupportedOperationException();
    }
}
