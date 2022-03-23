package cz.cvut.fel.pjv.chess.players;

public abstract class Player {
    private final int color;

    public Player(int color) {
        this.color = color;
    }

    public void makeMove() {
        throw new UnsupportedOperationException();
    }

    public boolean won() {
        throw new UnsupportedOperationException();
    }

    public boolean lost() {
        throw new UnsupportedOperationException();
    }
}
