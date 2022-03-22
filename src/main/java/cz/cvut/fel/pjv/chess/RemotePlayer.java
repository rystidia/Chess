package cz.cvut.fel.pjv.chess;

public class RemotePlayer extends Player{
    public RemotePlayer(int color) {
        super(color);
    }

    public void makeMove() {
        throw new UnsupportedOperationException();
    }
}

