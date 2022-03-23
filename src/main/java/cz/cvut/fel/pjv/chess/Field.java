package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;

public class Field {
    private final int color;
    private final VerticalFile vFile;
    private final int hRank;
    private Figure figure;
    private boolean isEmpty = true;

    public Field(int color, VerticalFile vFile, int hRank) {
        this.color = color;
        this.vFile = vFile;
        this.hRank = hRank;
    }

    public enum VerticalFile{
        a(1), b(2), c(3), d(4), e(5), f(6), g(7), h(8);

        public int number;

        VerticalFile(int number) {
            this.number = number;
        }
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

    public VerticalFile getVFile() {
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
