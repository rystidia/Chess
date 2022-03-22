package cz.cvut.fel.pjv.chess;

public abstract class Figure {
    private final int color;
    private final Field position;
    private final String image;
    private boolean isCaptured;

    public Figure(int color, Field position, String image) {
        this.color = color;
        this.position = position;
        this.image = image;
        this.isCaptured = false;
    }

    public void die() {
        this.isCaptured = true;
    }

    public Field[] getDiagonalDirections() {
        throw new UnsupportedOperationException();
    }

    public Field getPosition() {
        return this.position;
    }

    public Field[] getValidMoves() {
        throw new UnsupportedOperationException();
    }

    public Field[] getVertAndHorDirections() {
        throw new UnsupportedOperationException();
    }

    public boolean isCaptured() {
        return this.isCaptured;
    }

    public boolean move() {
        throw new UnsupportedOperationException();
    }
}
