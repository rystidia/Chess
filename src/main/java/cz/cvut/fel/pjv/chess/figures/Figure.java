package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.FieldOld;

import java.util.List;

/**
 *
 *
 * @author  pucilpet@fel.cvut.cz
 * @author  rystidia@fel.cvut.cz
 * @version 1.0
 */
public abstract class Figure {
    private final int color;
    private final FieldOld position;
    private final String image;
    private boolean isCaptured;

    public Figure(int color, FieldOld position, String image) {
        this.color = color;
        this.position = position;
        this.image = image;
        this.isCaptured = false;
    }

    public void die() {
        this.isCaptured = true;
    }

    public List<FieldOld> getDiagonalDirections() {
        throw new UnsupportedOperationException();
    }

    public FieldOld getPosition() {
        return this.position;
    }

    public List<FieldOld> getValidMoves() {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @return List of
     */
    public List<FieldOld> getVertAndHorDirections() {
        throw new UnsupportedOperationException();
    }

    public boolean isCaptured() {
        return this.isCaptured;
    }

    public boolean move() {
        throw new UnsupportedOperationException();
    }
}
