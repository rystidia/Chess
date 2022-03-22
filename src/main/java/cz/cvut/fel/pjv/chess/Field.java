package cz.cvut.fel.pjv.chess;

public class Field {
    private final int color;
    private final char vFile;
    private final int hRank;
    private Figure figure;
    private boolean isEmpty = true;

    public Field(int color, char vFile, int hRank) {
        this.color = color;
        this.vFile = vFile;
        this.hRank = hRank;
    }

    public int getColor() {
        return this.color;
    }

    public Figure getFigure() {
        return this.figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
        isEmpty = false;
    }

    public char getVFile() {
        return this.vFile;
    }

    public int getHRank() {
        return this.hRank;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void removeFigure() {
        this.figure = null;
        isEmpty = true;
    }
}
